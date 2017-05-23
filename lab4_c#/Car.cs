using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace laborka_4
{

    public class Car : IComparable<Car>
    {
        private string v1;
        private int v2;

        public int CompareTo(Car other)
        {
            if (String.Compare(other.Engine.Model, this.Engine.Model) > 0)
                return 1;
            else if (String.Compare(other.Engine.Model, this.Engine.Model) < 0)
                return -1;
            else
            {
                if (other.Engine.Displacement > this.Engine.Displacement)
                    return 1;
                else if (other.Engine.Displacement < this.Engine.Displacement)
                    return -1;
                else
                {
                    if (other.Engine.HorsePower > this.Engine.HorsePower)
                        return 1;
                    else if (other.Engine.HorsePower < this.Engine.HorsePower)
                        return -1;
                    else
                        return 0;
                }
            }
        }

        public int Id { get; set; }
        public string Model { get; set; }
        public Engine Engine { get; set; }
        public int Year { get; set; }
        public string Benzyna { get; set; }
        public Car() { }
        public Car(string model, Engine engine, int year, string benzyna)
        {
            Model = model;
            Engine = engine;
            Year = year;
            Benzyna = benzyna;
        }

      
    }
}
