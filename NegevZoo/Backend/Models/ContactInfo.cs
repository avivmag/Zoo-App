using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend.Models
{
    public class ContactInfo
    {
        public int id { get; set; }
        public string via { get; set; }
        public string address { get; set; }
        public int language { get; set; }
    }
}
