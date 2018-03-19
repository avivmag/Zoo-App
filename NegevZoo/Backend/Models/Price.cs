using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class Price
    {
        public int id { get; set; }
        public string population { get; set; }
        public double pricePop { get; set; }
        public int language { get; set; }
    }
}
