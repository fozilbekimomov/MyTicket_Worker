package uz.fozilbekimomov.mysticker.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import uz.fozilbekimomov.mysticker.R
import uz.fozilbekimomov.mysticker.core.models.LocationModel
import uz.fozilbekimomov.mysticker.core.utils.*
import uz.fozilbekimomov.mysticker.databinding.FragmentHomeBinding
import java.io.ByteArrayOutputStream


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var isGPSEnabled = false

    var sp: SharedPreferences? = null
    var byte: ByteArray? = null
    var locationModel: LocationModel? = null
    var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        GpsUtils(requireActivity()).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@HomeFragment.isGPSEnabled = isGPSEnable
            }
        })

        binding.homeCapButton.setOnClickListener {

            dispatchTakePictureIntent()

        }

        binding.logOut.setOnClickListener {
//            sp?.edit()?.clear()?.apply()
//            activity?.onBackPressed()
        }

        binding.sendData.setOnClickListener {

            Log.d(TAG, "onViewCreated: locationModel-> ${locationModel != null}")

            sendData()

        }

        setObservers()

    }

    private fun sendData() {
        if (imageUrl.length > 0) {

            locationModel?.let {

                homeViewModel.uploadUserData(
                    imageUrl,
                    sp?.getString(USER_NAME, "")!!,
                    sp?.getString(USER_NUMBER, "")!!,
                    it
                )
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.home_title),
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    private fun setObservers() {

        homeViewModel.imageUrl.observe(viewLifecycleOwner, { url ->

            Log.d(TAG, "setObservers: $url")
            this.imageUrl = url
            sendData()
        })


        homeViewModel.errorData.observe(viewLifecycleOwner, { error ->

            Log.d(TAG, "setObservers: $error")

        })

        homeViewModel.userData.observe(viewLifecycleOwner, {

            Log.d(TAG, "setObservers: $it")
            Toast.makeText(requireContext(), "Успешно отправлен", Toast.LENGTH_SHORT).show()

            binding.progressLayout.visibility = View.GONE

            binding.homeImage.setImageResource(R.drawable.ic_baseline_image_24)

        })

    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> {
                Snackbar.make(requireView(), getString(R.string.enable_gps), Snackbar.LENGTH_LONG)
                    .show()
            }

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> {

                Snackbar.make(
                    requireView(),
                    getString(R.string.permission_request),
                    Snackbar.LENGTH_LONG
                ).show()

            }

            else -> if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    LOCATION_REQUEST
                )
            }else{
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_REQUEST
                )
            }
        }
    }

    private fun startLocationUpdate() {
        homeViewModel.getLocationData().observe(this, Observer {
            this.locationModel = it
            binding.gpsText.text = getString(R.string.latLong, it.longitude, it.latitude)
        })
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.homeImage.setImageBitmap(imageBitmap)

//            binding.sendData.visibility = View.VISIBLE

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            byte = baos.toByteArray()

            byte?.let {
                binding.progressLayout.visibility = View.VISIBLE
                homeViewModel.uploadImage(it, "${sp?.getString(USER_NAME, "")}")
            }

        }
        if (requestCode == GPS_REQUEST) {
            isGPSEnabled = true
            invokeLocationAction()
        }
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}