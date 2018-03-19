using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Backend.Models
{
    public class Animal
    {
        public int id { get; set; }
        public string name { get; set; }
        public string story { get; set; }
        public int encId { get; set; }
        public int language { get; set; }
    }
}