using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace FlexAutomator
{
    public partial class Form1 : Form
    {
        private Point _rectStartPoint;
        private Rectangle _rect = new Rectangle();
        private Brush _selectionBrush = new SolidBrush(Color.Transparent);
        private bool _isMouseDown = false;
        private Pen _drawingPen = new Pen(new SolidBrush(Color.Red));
        private Image _screenImage;
        private Graphics _graphics;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            FileInfo[] imageFiles = new DirectoryInfo(Globals.ImagesFolder).GetFiles("*." + Globals.ImageExtension);
            comboBox1.Items.Clear();
            foreach (var imageFile in imageFiles)
            {
                comboBox1.Items.Add(imageFile.Name.Replace("." + Globals.ImageExtension, ""));
            }
        }

        protected override void OnPaintBackground(PaintEventArgs e)
        {
            
        }

        private void Capture_Click(object sender, EventArgs e)
        {
            
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            if (!_isMouseDown)
                Invalidate();
        }

        private void button9_Click(object sender, EventArgs e)
        {
           
        }

        private void pictureBox1_MouseDown(object sender, MouseEventArgs e)
        {
            _rect = new Rectangle();
            _rectStartPoint = e.Location;
            _isMouseDown = true;
            Invalidate();
        }

        private void pictureBox1_MouseMove(object sender, MouseEventArgs e)
        {
            if(e.Button != MouseButtons.Left)
            {

                return;
            }

            Point tempEndPoint = e.Location;
            _rect.Location = new Point(
                Math.Min(_rectStartPoint.X, tempEndPoint.X),
                Math.Min(_rectStartPoint.Y, tempEndPoint.Y)            
             );

            _rect.Size = new Size(
                Math.Abs(_rectStartPoint.X - tempEndPoint.X),
                Math.Abs(_rectStartPoint.Y - tempEndPoint.Y)
             );

            pictureBox1.Invalidate();
        }

        private void pictureBox1_MouseUp(object sender, MouseEventArgs e)
        {
            if(!_isMouseDown)
                return;

            if(_rect.Height == 0 || _rect.Width==0)
            {
                return;
            }

            _isMouseDown = false;
            Point StartPoint = new Point(_rect.X, _rect.Y);

            Rectangle bounds = new Rectangle(_rect.Left, _rect.Top, 0, 0);
            
            Rectangle sourceRect = new Rectangle(_rect.Left, _rect.Top, _rect.Width, _rect.Height);
            Rectangle destRect = new Rectangle(0,0, _rect.Width, _rect.Height);

            using(Bitmap bitmap = new Bitmap(_rect.Width, _rect.Height))
            {
                using(Graphics g = Graphics.FromImage(bitmap))
                {
                    //g.CopyFromScreen(_rect.Left, _rect.Top,0, 0, _rect.Size);
                    
                    g.DrawImage(pictureBox1.Image, destRect, sourceRect, GraphicsUnit.Pixel);
                    Image img = (Image) bitmap;
                    Clipboard.SetImage((img));
                    _rect = new Rectangle();
                    _rect.Height = 0;
                    _rect.Width = 0;
                    
                    
                    SaveScreenshot saveScreenShot = new SaveScreenshot();
                    saveScreenShot.ShowDialog();
                }
            }

            
        }

        private void pictureBox1_Paint(object sender, PaintEventArgs e)
        {
            if(pictureBox1.Image != null)
            {
                if(_rect != null && _rect.Width > 0 && _rect.Height > 0)
                {
                    e.Graphics.DrawRectangle(_drawingPen,_rect);
                    _graphics = e.Graphics;
                }
            }
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void btnCapture_Click(object sender, EventArgs e)
        {
            Thread.Sleep(3000);
            Image capturedImage = ScreenCapture.CaptureScreen();
            Thread.Sleep(500);
            _screenImage = Clipboard.GetImage();
            pictureBox1.Image = _screenImage;
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            string imageName = comboBox1.SelectedItem.ToString();
            WriteCode("find" + Globals.Delim + imageName);
        }

        private void WriteCode(string code)
        {
            StreamWriter sw = new StreamWriter(Globals.CodeFile);
            sw.WriteLine(code);
            sw.Close();
        }
    }
}
