using BL;
using DAL;
using DAL.Models;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Linq;

namespace ZooTests.Unit_Tests
{
    [TestClass]
    public class AnimalUnitTests
    {
        private ZooContext context;
        private int nonExistantLang;

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            context = new ZooContext();
            nonExistantLang = 10000;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            DummyDB.CleanDb();
        }

        #endregion

        #region GetAllAnimalsResults
        [TestMethod]
        public void GetAllAnimalResultsLangHe()
        {
            Assert.AreEqual(3, context.GetAnimalsResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllAnimalResultsLangEn()
        {
            Assert.AreEqual(3, context.GetAnimalsResults((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAllAnimalResultsLangAr()
        {
            Assert.AreEqual(0, context.GetAnimalsResults((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAllAnimalsLanguageNotExist()
        {
            context.GetAnimalsResults(nonExistantLang);
        }

        #endregion

        #region GetAnimalResultsWithStory
        [TestMethod]
        public void GetAnimalResultsWithStoryLangHe()
        {
            Assert.AreEqual(2, context.GetAnimalResultsWithStory((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAnimalResultsWithStoryLangEn()
        {
            Assert.AreEqual(2, context.GetAnimalResultsWithStory((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAnimalResultsWithStoryLangAr()
        {
            Assert.AreEqual(0, context.GetAnimalResultsWithStory((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAllAnimalResultsWithStoryLanguageNotExist()
        {
            context.GetAnimalResultsWithStory(nonExistantLang);
        }

        #endregion

        #region GetAnimalById
        [TestMethod]
        public void GetAnimalByIdValidInput()
        {
            var animal = context.GetAnimalById(1, (int)Languages.en);
            Assert.IsInstanceOfType(animal, typeof(AnimalResult));

            Assert.AreEqual(1, animal.Id);
            Assert.AreEqual("Olive Baboon", animal.Name);
        }

        [TestMethod]
        public void GetAnimalByIdNoDataInWantedLang()
        {
            var animal = context.GetAnimalById(1, (int)Languages.ar);
            Assert.IsInstanceOfType(animal, typeof(AnimalResult));

            Assert.AreEqual(1, animal.Id);
            Assert.AreEqual("בבון הזית", animal.Name);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. animal id doesn't exsits")]
        public void GetAnimalByIdWrongId()
        {
            context.GetAnimalById(-4, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAnimalByIdLanguageNotExist()
        {
            context.GetAnimalById(2, nonExistantLang);
        }

        #endregion

        #region GetAnimalResultByEnclosure
        [TestMethod]
        public void GetAnimalResultByEnclosureValidInput()
        {
            var animals = context.GetAnimalResultByEnclosure(1, (int)Languages.he);
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        public void GetAnimalResultByEnclosureNoDataInWantedLangauge()
        {
            var animals = context.GetAnimalResultByEnclosure(1, (int)Languages.ar);
            Assert.AreEqual(2, animals.Count());

            var an = animals.First();
            Assert.AreEqual((int)Languages.he, an.Language);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure doesn't exists")]
        public void GetAnimalResultByEnclosureEncIdDoesntExists()
        {
            context.GetAnimalResultByEnclosure(-4, -4);
        }

        #endregion

        #region GetAnimalByName
        [TestMethod]
        public void GetAnimalByNameValidInputFullName()
        {
            var animals = context.GetAnimalByName("בבון הזית", (int)Languages.he);
            Assert.AreEqual(1, animals.Count());

            var olive = animals.SingleOrDefault(a => a.Name == "בבון הזית");
            Assert.AreEqual(olive.Id, 1);
        }

        [TestMethod]
        public void GetAnimalByNameValidInputPartName()
        {
            var animals = context.GetAnimalByName("on", (int)Languages.en);
            Assert.AreEqual(2, animals.Count());

            var olive = animals.SingleOrDefault(a => a.Name == "Olive Baboon");
            Assert.AreEqual(olive.Id, 1);

            var monkey = animals.SingleOrDefault(a => a.Name == "Monkey");
            Assert.AreEqual(monkey.Id, 3);

            var gorila = animals.SingleOrDefault(a => a.Name == "Gorilla");
            Assert.IsNull(gorila);
        }

        [TestMethod]
        public void GetAnimalByNameValidNameWrongLang()
        {
            var animals = context.GetAnimalByName("on", (int)Languages.he);
            Assert.AreEqual(0, animals.Count());
        }

        [TestMethod]
        public void GetAnimalByNameAnimalNameDoesntexists()
        {
            var animals = context.GetAnimalByName("abcdefghijklmnop", (int)Languages.en);
            Assert.AreEqual(0, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAnimalByNameLanguageNotExist()
        {
            context.GetAnimalByName("Monkey", nonExistantLang);
        }
        #endregion

        #region GetAllAnimal
        [TestMethod]
        public void GetAllAnimal()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = animals.First();

            Assert.AreEqual(1, an.id);
            Assert.AreEqual("בבון הזית", an.name);
            Assert.AreEqual(1, an.enclosureId);
        }

        #endregion

        #region GetAllAnimalsDetailsById
        [TestMethod]
        public void GetAllAnimalDetailsByIdValidInput()
        {
            var animals = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, animals.Count());

            var an = animals.First();
            Assert.AreEqual(1, an.animalId);
            Assert.AreEqual("קופים", an.category);

            an = animals.Last();
            Assert.AreEqual(1, an.animalId);
            Assert.AreEqual("Monkies", an.category);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure doesn't exists")]
        public void GetAllAnimalDetailsByIdWrongId()
        {
            context.GetAllAnimalsDetailById(-4);
        }

        #endregion

        #region GetAnimalByEnclosure
        [TestMethod]
        public void GetAnimalByEnclosureValidInput()
        {
            var animals = context.GetAnimalsByEnclosure(1);
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure doesn't exists")]
        public void GetAnimalByEnclosureEncIdDoesntExists()
        {
            context.GetAnimalsByEnclosure(-4);
        }

        #endregion

        #region UpdateAnimal

        [TestMethod]
        public void UpdateAnimalAddAnValidTest()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "הקקי שלי",
                enclosureId = 2,
            };

            context.UpdateAnimal(an);

            animals = context.GetAllAnimals();
            Assert.AreEqual(4, animals.Count());
        }

        //[TestMethod]
        //[ExpectedException(typeof(ArgumentException), "Wrong input in adding animal. Animal name already exists")]
        //public void UpdateAnimalAddAnimalNameExists()
        //{
        //    var animals = context.GetAllAnimals();
        //    Assert.AreEqual(3, animals.Count());

        //    var an = new Animal
        //    {
        //        id = default(int),
        //        name = "קוף",
        //        enclosureId = 3,
        //    };

        //    context.UpdateAnimal(an);
        //}

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Animal name is null or null")]
        public void UpdateAnimalWrongName()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "     ",
                enclosureId = 2,
            };

            context.UpdateAnimal(an);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Enclosure id doesn't exists")]
        public void UpdateAnimalWrongEncId()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "הקקי שלי",
                enclosureId = -2,
            };

            context.UpdateAnimal(an);
        }

        [TestMethod]
        public void UpdateAnimalValidInput()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = animals.SingleOrDefault(a => a.name == "גורילה");

            an.name = "גורילה מנייאקית";

            context.UpdateAnimal(an);

            animals = context.GetAllAnimals();
            Assert.IsTrue(animals.Any(a => a.name == "גורילה מנייאקית"));
            Assert.IsFalse(animals.Any(a => a.name == "גורילה"));
            Assert.AreEqual(3, animals.Count());
        }

        [TestMethod]
        public void UpdateAnimalValidButMissingInput()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = animals.SingleOrDefault(a => a.name == "גורילה");

            an.pictureUrl = "";

            context.UpdateAnimal(an);

            animals = context.GetAllAnimals();
            Assert.IsTrue(animals.Any(a => a.pictureUrl == ""));
            Assert.AreEqual(3, animals.Count());
        }

        //[TestMethod]
        //[ExpectedException(typeof(ArgumentException), "Wrong input in updating animal. Animal name already exitst")]
        //public void UpdateAnimalNameAlreadyExists()
        //{
        //    var animals = context.GetAllAnimals();
        //    Assert.AreEqual(3, animals.Count());

        //    var an = new Animal
        //    {
        //        id = 2,
        //        name = "גורילה",
        //        enclosureId = 1,
        //    };

        //    an.name = "קוף";

        //    context.UpdateAnimal(an);
        //}

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Animal id does'nt exits")]
        public void UpdateAnimalWrongId()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = 3,
                name = "גורילה",
                enclosureId = 1
            };

            an.id = -3;

            context.UpdateAnimal(an);
        }
        #endregion

        #region UpdateAnimalDetails

        [TestMethod]
        public void UpdateAnimalDetailsAddAnValidTest()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var det = new AnimalDetail
            {
                animalId = 1,
                name = "הקקי שלי",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            context.UpdateAnimalDetails(det);

            details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(3, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input in adding animal. Animal name already exists")]
        public void UpdateAnimalDetailsAddAnimalNameExists()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                name = "בבון הזית",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            context.UpdateAnimalDetails(an);

        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The name is null or whitespace.")]
        public void UpdateAnimaDetailsWrongName()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            context.UpdateAnimalDetails(an);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The animal id coesnt exists.")]
        public void UpdateAnimalDetailsAddAnimalDoesntExists()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = -1,
                name = "בבון הזית",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            context.UpdateAnimalDetails(an);

        }

        [TestMethod]
        public void UpdateAnimalDetailsValidInput()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                language = (long)Languages.he,
                name = "בבון הזית",
                category = "קופים",
                series = "קוף",
                distribution = "",
                family = "",
                food = "",
                reproduction = "",
                story = "גילאור בבון הזית מאוד חמוד"
            };

            an.name = "אביב מאג";

            context.UpdateAnimalDetails(an);

            details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());
            Assert.IsTrue(details.Any(d => d.name == "אביב מאג"));
            Assert.IsFalse(details.Any(d => d.name == "בבון הזית"));

        }

        [TestMethod]
        public void UpdateAnimalDetailsValidButMissingInput()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                language = (long)Languages.he,
                name = "בבון הזית",
                category = "קופים",
                series = "קוף",
                distribution = "",
                family = "",
                food = "",
                reproduction = "",
                story = "גילאור בבון הזית מאוד חמוד"
            };

            an.category = "";
            an.series = "";

            context.UpdateAnimalDetails(an);

            details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            an = details.SingleOrDefault(d => d.language == (int)Languages.he);
            Assert.IsNotNull(an);
            Assert.AreEqual("", an.category);
            Assert.AreEqual("", an.series);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input in updating animal. Animal name already exitst")]
        public void UpdateAnimalDetailNameAlreadyExists()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                language = (long)Languages.he,
                name = "בבון הזית",
                category = "קופים",
                series = "קוף",
                distribution = "",
                family = "",
                food = "",
                reproduction = "",
                story = "גילאור בבון הזית מאוד חמוד"
            };
            an.name = "גורילה";

            context.UpdateAnimalDetails(an);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void UpdateAnimalDetailsWrongLangauge()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                language = -4,
                name = "בבון הזית",
                category = "קופים",
                series = "קוף",
                distribution = "",
                family = "",
                food = "",
                reproduction = "",
                story = "גילאור בבון הזית מאוד חמוד"
            };
            an.name = "גורילה";

            context.UpdateAnimalDetails(an);
        }
        #endregion

        #region DeleteAnimal
        [TestMethod]
        public void DeleteAnimalValidInput()
        {
            var animals = context.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var monk = animals.SingleOrDefault(en => en.name == "קוף");
            Assert.IsNotNull(monk);

            var details = context.GetAllAnimalsDetailById((int)monk.id);
            Assert.AreEqual(2, details.Count());

            context.DeleteAnimal((int)monk.id);
            animals = context.GetAllAnimals();
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Animal doesn't exists")]
        public void DeleteAnimalIdDoesntExists()
        {
            context.DeleteAnimal(-4);
        }


        #endregion

    }
}
