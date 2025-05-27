package colour_constraints
import android.graphics.Color
import com.example.row.implementation.Flags

class ColourHomePage {


    object ColorUtils {
        @JvmStatic
        fun getIconColor(): String {
            // Logic to determine color
            return Flags.getFlagColour();
        }
    }
}