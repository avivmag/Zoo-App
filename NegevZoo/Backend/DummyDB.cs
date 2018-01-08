using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend.Models;
using Backend;

namespace Backend
{
    public class DummyDB
    {
        private List<Enclosure> Enclosures = new List<Enclosure>
        {
            new Enclosure
            {
                Id          = 1,
                Name        = "Monkeys",
                Story       = "Our monkeys have been great! They are awesome.",
                Language    = (int)Languages.en
            },

            new Enclosure
            {
                Id          = 1,
                Name        = "תצוגת הקופים",
                Story       = "הקופים שלנו הם הכי הכי!",
                Language    = (int)Languages.he
            }
        };

        private List<Animal> Animals        = new List<Animal>
        {
            new Animal
            {
                Id          = 1,
                Name        = "Olive Baboon",
                EncId       = 1,
                Story       = "Gilor the olive baboon is very lovable.",
                Language    = (int)Languages.en
            },

            new Animal
            {
                Id          = 1,
                Name        = "בבון הזית",
                EncId       = 1,
                Story       = "גילאור בבון הזית מאוד חמוד",
                Language    = (int)Languages.he
            },

            new Animal
            {
                Id          = 2,
                Name        = "Gorilla",
                EncId       = 1,
                Story       = "Shrek the mighty gorilla!",
                Language    = (int)Languages.en
            },

            new Animal
            {
                Id          = 2,
                Name        = "גורילה",
                EncId       = 1,
                Story       = "שרק הוא וואחד גורילה!",
                Language    = (int)Languages.he
            },

            new Animal
            {
                Id          = 3,
                Name        = "Monkey",
                EncId       = 1,
                Story       = "Kofifo is Marco's monkey.",
                Language    = (int)Languages.en
            },

            new Animal
            {
                Id          = 3,
                Name        = "קוף",
                EncId       = 1,
                Story       = "קופיקו הוא הקוף של מרקו.",
                Language    = (int)Languages.he
            }
        };

        /// <summary>
        /// Dummy Getter for db enclosures.
        /// </summary>
        /// <returns>The dummy enclosures.</returns>
        public IEnumerable<Enclosure> GetEnclosures(int language)
        {
            return this.Enclosures.Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Dummy getter for db animals.
        /// </summary>
        /// <returns>The dummy animals.</returns>
        public IEnumerable<Animal> GetAnimals(int language)
        {
            return this.Animals.Where(a => a.Language == language).ToArray();
        }

        /// <summary>
        /// Dummy setter for db enclosures.
        /// </summary>
        /// <param name="enclosures">The enclosures to set.</param>
        public void SetEnclosures(IEnumerable<Enclosure> enclosures)
        {
            this.Enclosures = new List<Enclosure>(enclosures);
        }

        /// <summary>
        /// Dummy setter for db animals.
        /// </summary>
        /// <param name="animals">The animals to set.</param>
        public void SetAnimals(IEnumerable<Animal> animals)
        {
            this.Animals = new List<Animal>(animals);
        }
    }
}
