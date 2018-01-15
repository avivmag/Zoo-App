using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class Price
    {
        public int Id { get; set; }
        public string Population { get; set; }
        public double PricePop { get; set; }
        public int Language { get; set; }
    }
}
