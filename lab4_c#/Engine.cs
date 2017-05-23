using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace laborka_4
{
    public class Engine
    {
        public int Id { get; set; }
        public double Displacement { get; set; }
        public double HorsePower { get; set; }
        public String Model { get; set; }
        public Engine() { }
        public Engine(double displacement, double horsePower, string model)
        {
            this.Displacement = displacement;
            this.HorsePower = horsePower;
            this.Model = model;
        }
        public override string ToString()
        {
            return Model + " " + Displacement + " (" + HorsePower + " hp) ";
        }
    }
}
