using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class OpeningHour
    {
        public int Id { get; set; }
        public string Day { get; set; }
        public double StartHour { get; set; }
        public double EndHour { get; set; }
        public int Language { get; set; }
    }
}
