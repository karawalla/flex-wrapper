using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace FlexAutomator
{
    public partial class SaveScreenshot : Form
    {
        public SaveScreenshot()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string imageName = textBox1.Text;
            string fullImageFileName = Globals.ImagesFolder + @"\" + imageName + "." + Globals.ImageExtension;

            Clipboard.GetImage().Save(fullImageFileName);
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void SaveScreenshot_Load(object sender, EventArgs e)
        {
            pictureBox1.Image = Clipboard.GetImage();
        }
    }
}
