using System;
using System.Collections.Generic;
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
using System.Windows.Shapes;

namespace laborka_4
{
    /// <summary>
    /// Interaction logic for Window1.xaml
    /// </summary>
    public partial class Window1 : Window
    {
        MainWindow w;
        int x;
        Car car;
        public Window1(MainWindow w, Car car, int x)
        {
            InitializeComponent();
            this.w = w;
            this.car = car;
            this.x = x;
            if (x == 1)
            {

                yearBox.Text = car.Year.ToString();
                horseBox.Text = car.Engine.HorsePower.ToString();
                disBox.Text = car.Engine.Displacement.ToString();
                engineBox.Text = car.Engine.Model.ToString();
                modelBox.Text = car.Model.ToString();

            }
        }

        private void save_button_Click(object sender, RoutedEventArgs e)
        {
            if (x == 0)
            {
                if (modelBox != null && yearBox != null && disBox != null && horseBox != null && engineBox != null)
                {
                    MyDbContext ctx = new MyDbContext();
                    double dis = double.Parse(disBox.Text);
                    double hor = double.Parse(horseBox.Text);
                    int yer = int.Parse(yearBox.Text);
                    Car car2 = new Car(modelBox.Text, new Engine(dis, hor, engineBox.Text), yer, "petrol");
                    //Car car = new Car("ZZZ", new Engine(2.6, 300, "WGI"), 1987, "diesel");
                    ctx.Cars.Add(car2);
                    ctx.SaveChanges();
                    w.refreshGrid();
                    this.Close();
                }
            }
            else if (x == 1)
            {
                MyDbContext ctx = new MyDbContext();
                var car1 = ctx.Cars.Where(x => x.Id == car.Id).FirstOrDefault();
                car1.Engine = new Engine(car.Engine.Displacement, car.Engine.HorsePower, car.Engine.Model);

                if (modelBox != null && yearBox != null && disBox != null && horseBox != null && engineBox != null)
                {
                    double dis = double.Parse(disBox.Text);
                    double hor = double.Parse(horseBox.Text);
                    int yer = int.Parse(yearBox.Text);
                    car1.Year = yer;
                    car1.Engine.Displacement = dis;
                    car1.Engine.HorsePower = hor;
                    car1.Model = modelBox.Text;
                    car1.Engine.Model = engineBox.Text;
                    ctx.SaveChanges();
                    w.refreshGrid();
                    this.Close();
                }
            }
        }
    }
}
