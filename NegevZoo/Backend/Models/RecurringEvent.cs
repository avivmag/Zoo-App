using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class RecurringEvent
    {
        public int Id { get; set;}
        public string Descroption { get; set; }
        public string Day { get; set; }
        public DateTime StartHour { get; set; }
        public DateTime EndHour { get; set; }
        public int Language { get; set; }
        public int EncId { get; set; }
    }
}
