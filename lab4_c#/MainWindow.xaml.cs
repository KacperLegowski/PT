using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace laborka_4
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        Window1 w;
        MyDbContext ctx;
        public MainWindow()
        {
            InitializeComponent();
            initializeDB();

        }
        private void initializeDB()
        {
            ctx = new MyDbContext();
            List<Car> carsList = ctx.Cars.Include("Engine").ToList();
            carsList.Sort();
            //<Car> carsList = ctx.Cars.Include("Engine").To;
            dataGrid.ItemsSource = carsList;
        }

        private void find_button_Click(object sender, RoutedEventArgs e)
        {
            ctx = new MyDbContext();
            List<Car> carsList = ctx.Cars.Include("Engine").ToList();
            List<Car> tmp = new List<Car>();
            carsList.Sort();
            foreach (var de in carsList)
            {
                if (comboBox.SelectedItem == model)
                {
                    if (de.Model.Contains(textBox.Text))
                    {
                        tmp.Add(de);
                    }
                }
                else
                {
                    if (de.Year.ToString() == textBox.Text)
                    {
                        tmp.Add(de);
                    }
                }

            }
            //<Car> carsList = ctx.Cars.Include("Engine").To;
            dataGrid.ItemsSource = tmp;
        }
        public void refreshGrid()
        {
            InitializeComponent();
            initializeDB();
        }
        private void add_button_Click(object sender, RoutedEventArgs e)
        {
            w = new Window1(this, null, 0);
            w.Owner = this;
            w.Show();
        }
        private void DataGrid_MouseDoubleClick(object sender, RoutedEventArgs e)
        {
            var currentRowIndex = dataGrid.Items.IndexOf(dataGrid.SelectedItem);
            {
                if (dataGrid.SelectedItem != null)
                {
                    Window1 wm = new Window1(this, (Car)dataGrid.SelectedItem, 1);
                    wm.Show();
                }

            }
        }
    }
}
