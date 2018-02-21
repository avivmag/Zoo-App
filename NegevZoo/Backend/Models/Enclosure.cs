using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class Enclosure
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Story { get; set; }
        public int Language { get; set; }
        public Double Longtitude { get; set; }
        public Double Latitude{ get; set; }

    }
}
