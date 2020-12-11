package uz.fozilbekimomov.mysticker.core.models


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/10/20
 * @project MySticker
 */


data class UserData(
    var userName: String,
    var phoneNumber:String,
    var addedTime: Long,
    var imageUrl:String,
    var location:LocationModel
)