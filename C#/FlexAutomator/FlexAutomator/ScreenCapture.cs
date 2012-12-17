using System.Drawing;
using System.Windows.Forms;

namespace FlexAutomator
{
    public class ScreenCapture
    {
        internal static Image CaptureScreen()
        {
            Rectangle screenBounds = Screen.GetBounds(Point.Empty);

            using(Bitmap bitmap = new Bitmap(screenBounds.Width, screenBounds.Height))
            {
                using(Graphics g = Graphics.FromImage(bitmap))
                {
                    g.CopyFromScreen(Point.Empty, Point.Empty, screenBounds.Size);
                    Image img = (Image) bitmap;
                    Clipboard.SetImage((img));
                    return (Image) bitmap;
                }
            }

        }
    }
}
