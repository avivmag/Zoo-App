using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend;
using Backend.Models;

namespace DAL
{
    public class DBContext : IDisposable
    {
        private static DummyDB db = null;

        public DBContext()
        {
            if (db == null)
            {
                db = new DummyDB();
            }
        }

        
        /// <summary>
        /// Dummy Getter for db enclosures.
        /// </summary>
        /// <returns>The dummy enclosures.</returns>
        public IEnumerable<Enclosure> GetEnclosures(int language)
        {
            return db.Enclosures.Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Dummy getter for db animals.
        /// </summary>
        /// <returns>The dummy animals.</returns>
        public IEnumerable<Animal> GetAnimals(int language)
        {
            return db.Animals.Where(a => a.Language == language).ToArray();
        }

        public Animal GetAnimalById(int language, int id)
        {
            return db.Animals.SingleOrDefault(a => a.Language == language && a.Id == id);
        }

        /// <summary>
        /// Dummy setter for db enclosures.
        /// </summary>
        /// <param name="enclosures">The enclosures to set.</param>
        public void SetEnclosures(IEnumerable<Enclosure> enclosures)
        {
            db.Enclosures = new List<Enclosure>(enclosures);
        }

        /// <summary>
        /// Dummy setter for db animals.
        /// </summary>
        /// <param name="animals">The animals to set.</param>
        public void SetAnimals(IEnumerable<Animal> animals)
        {
            db.Animals = new List<Animal>(animals);
        }

        public void Dispose()
        {
            // dummyDB.saveChanges()
        }
    }
}
