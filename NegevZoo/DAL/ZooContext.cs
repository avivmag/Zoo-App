using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend;
using Backend.Models;
using System.Data.Entity;

namespace DAL
{
    public class ZooContext : IDisposable 
    {
        private IZooDB zooDB;

        // TODO:: Mark isTesting to false when the database it ready.
        public ZooContext(bool isTesting = true)
        {

            if (isTesting)
            {
                zooDB = new DummyDB();
            }
            else
            {
                // TODO:: whenever the database will be ready - initialize it here.
            }
        }

        
        /// <summary>
        /// Dummy Getter for db enclosures.
        /// </summary>
        /// <returns>The dummy enclosures.</returns>
        public IEnumerable<Enclosure> GetEnclosures(int language)
        {
            return zooDB.Enclosures.Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Dummy getter for db animals.
        /// </summary>
        /// <returns>The dummy animals.</returns>
        public IEnumerable<Animal> GetAnimals(int language)
        {
            return zooDB.Animals.Where(a => a.Language == language).ToArray();
        }

        public Animal GetAnimalById(int language, int id)
        {
            return zooDB.Animals.SingleOrDefault(a => a.Language == language && a.Id == id);
        }

        /// <summary>
        /// Dummy updater for db enclosures.
        /// </summary>
        /// <param name="enclosures">The enclosures to set.</param>
        public void AddEnclosures(IEnumerable<Enclosure> enclosures)
        {
            zooDB.Enclosures.AddRange(enclosures);
        }

        /// <summary>
        /// Dummy setter for db animals.
        /// </summary>
        /// <param name="animals">The animals to set.</param>
        public void AddAnimals(IEnumerable<Animal> animals)
        {
            zooDB.Animals.AddRange(animals);
        }

        public void Dispose()
        {
            // dummyDB.saveChanges()
        }
    }
}
