using System;
using System.Collections.Generic;
using System.Linq;
using Backend.Models;
using DAL;

namespace BL
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
        /// Gets the enclosures.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <returns>The enclosures.</returns>
        public IEnumerable<Enclosure> GetEnclosures(int language)
        {
            return zooDB.GetEnclosures().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Gets the animals.
        /// </summary>
        /// <param name="language">The animal's data language.</param>
        /// <returns>The animals.</returns>
        public IEnumerable<Animal> GetAnimals(int language)
        {
            return zooDB.GetAnimals().Where(a => a.Language == language).ToArray();
        }

        /// <summary>
        /// Gets the animal by Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="id">The animal's Id.</param>
        /// <returns>The animal.</returns>
        public Animal GetAnimalById(int language, int id)
        {
            return zooDB.GetAnimals().SingleOrDefault(a => a.Language == language && a.Id == id);
        }

        /// <summary>
        /// Updates The enclosure.
        /// </summary>
        /// <param name="enclosures">The enclosures to update.</param>
        public void UpdateEnclosure(Enclosure enclosure)
        {
            var enclosures = zooDB.GetEnclosures();
            if (!enclosures.Contains(enclosure))
            {
                enclosures.Add(enclosure);
            }
        }

        /// <summary>
        /// Updates the animal.
        /// </summary>
        /// <param name="animals">The animal to update.</param>
        public void UpdateAnimal(Animal animal)
        {
            var animals = zooDB.GetAnimals();
            if (!animals.Contains(animal))
            {
                animals.Add(animal);
            }
        }

        /// <summary>
        /// Gets all the feed walls messages.
        /// </summary>
        /// <param name="language">The wallfeed's data language</param>
        /// <returns>The WallFeeds messages.</returns>
        public IEnumerable<WallFeed> GetWallFeed(int language)
        {
            return zooDB.GetWallFeed().Where(e => e.Language == language).ToArray();
        }



        /// <summary>
        /// Gets all the Price elements.
        /// </summary>
        /// <param name="language">The Price's data language.</param>
        /// <returns>The prices entitiess.</returns>
        public IEnumerable<Price> GetPrices(int language)
        {
            return zooDB.GetPrices().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Gets the zoo's about info.
        /// </summary>
        /// <param name="language">The language the about info is in.</param>
        /// <returns>The zoo's about info.</returns>
        public IEnumerable<String> GetZooAboutInfo(int language)
        {
            return zooDB.GetGeneralInfo()
                .Where(ge => ge.Language == language)
                .Select(ge => ge.aboutUs)
                .ToArray();
        }

        public void Dispose()
        {
            // zooDB.saveChanges();
        }
    }
}
