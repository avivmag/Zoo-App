using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class OpeningHour
    {
        public int id { get; set; }
        public string day { get; set; }
        public int startHour { get; set; }
        public int startMin { get; set; }
        public int endHour { get; set; }
        public int endMin { get; set; }
        public int language { get; set; }
    }
}
