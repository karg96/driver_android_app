package cos.tuk_tuk_driver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.util.lruCache
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cos.tuk_tuk_driver.DriverApp.Companion.context
import cos.tuk_tuk_driver.listener.MyButtonClickListener

class MyButton(
    val context: Context,
    val text: String,
    val textSize: Int,
    val imageResId: Int,
    val color: Int,
    val listener: MyButtonClickListener
) {


    private var pos: Int = 0
    private var clickRegion: RectF? = null
    private var resources: Resources

    init {
        resources = context.resources

    }

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion!!.contains(x, y)) {
            listener.onclick(pos)
            return true
        }
        return false

    }


    fun onDraw(c: Canvas, rectF: RectF, pos: Int) {
        val p = Paint()
        p.color = color
        c.drawRect(rectF, p)

        p.color = Color.WHITE
        p.textSize - textSize.toFloat()

        var r = Rect()
        val cHeight = rectF.height()
        val cWidth = rectF.width()
        p.textAlign = Paint.Align.RIGHT
        p.getTextBounds(text, 0, text.length, r)

        var x = 0f
        var y = 0f
        if (imageResId == 0) {
            x = cWidth / 2f - r.width() / 2f - r.left.toFloat()
            y = cHeight / 2f + r.height() / 2f - r.bottom.toFloat()
            c.drawText(text, rectF.left + x, rectF.top + y, p)
        } else {
            val d = ContextCompat.getDrawable(context, imageResId)
            val bitmap = drawableToBitmap(d)
//            c.drawBitmap(bitmap, (rectF.left + rectF.right) / 2, (rectF.top + rectF.bottom) / 2, p)
            c.drawBitmap(bitmap, (rectF.left + rectF.right) / 2, (rectF.top + rectF.bottom) / 2, p)
        }

        clickRegion = rectF
        this.pos = pos
    }


    private fun drawableToBitmap(d: Drawable?): Bitmap {

        if (d is BitmapDrawable) return d.bitmap
        val bitmap =
            Bitmap.createBitmap(d!!.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)

        d.draw(canvas)
        return bitmap
    }

}