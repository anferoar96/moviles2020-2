using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace Reto10
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            InitializeComponent();
        }
        private async void BtnCallWS_Click(object sender, EventArgs e)
        {
            WSClient client = new WSClient();
            String anho = (string)year.SelectedItem;
            String peri = (string)periodo.SelectedItem;
            String total = "https://www.datos.gov.co/resource/d8ii-85kx.json?ano=" + anho + "&periodo=0"+peri;
            var result = await client.Get<List<WSResult>>(total);
            string h = string.Empty;


            if (result != null)
            {
                foreach(var i in result)
                {
                    estrato1.Text = i.estrato_01;
                    estrato2.Text = i.estrato_02;
                    estrato3.Text = i.estrato_03;
                    estrato4.Text = i.estrato_04;
                    estrato5.Text = i.estrato_05;
                    estrato6.Text = i.estrato_06;

                }

            }

        }
    }
}
