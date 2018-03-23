using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class Enclosure
    {
        public int id { get; set; }
        public string name { get; set; }
        public string story { get; set; }
        public int language { get; set; }
        public Double longtitude { get; set; }
        public Double latitude { get; set; }
    }
}
