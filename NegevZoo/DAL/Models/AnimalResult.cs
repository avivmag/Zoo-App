using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class AnimalResult
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Interesting { get; set; }
        public long EncId { get; set; }
        public string Category { get; set; }
        public string Series { get; set; }
        public string Family { get; set; }
        public string Distribution { get; set; }
        public string Reproduction { get; set; }
        public string Food { get; set; }
        public string AudioUrl { get; set; }
        public long Preservation { get; set; }
        public string PictureUrl { get; set; }
        public long Language { get; set; }
    }
}
