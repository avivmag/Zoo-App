using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend.Models;
using Backend;
using System.Data.Entity;

namespace DAL
{
    /// <summary>
    /// Mock Database for unit testing.
    /// </summary>
    public class DummyDB : IZooDB
    {
        public DummyDB()
        {
            this.Animals         = new TestDbSet<Animal>();
            this.Enclosures      = new TestDbSet<Enclosure>();

            this.Animals.AddRange(InitializeAnimals());
            this.Enclosures.AddRange(InitializeEnclosures());
        }

        protected override List<TEntity> GetFromCache<TEntity>()
        {
            return null;
        }

        protected override void SetInCache<TEntity>(List<TEntity> entity)
        {
        }

        #region Initializers

        /// <summary>
        /// Initializes the animals mock.
        /// </summary>
        /// <returns>Mock animals list.</returns>
        private IEnumerable<Animal> InitializeAnimals()
        {
            return new List<Animal>
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
        }

        /// <summary>
        /// Initializes the enclosures mock.
        /// </summary>
        /// <returns>Mock enclosures list.</returns>
        private IEnumerable<Enclosure> InitializeEnclosures()
        {
            return new List<Enclosure>
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
        }

        #endregion
    }
}
