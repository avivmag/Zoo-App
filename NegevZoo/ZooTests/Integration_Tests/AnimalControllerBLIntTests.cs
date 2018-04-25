using NegevZoo.Controllers;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DAL;
using System.Web.Http;
using BL;
using DAL.Models;

namespace ZooTests
{
    [TestClass]
    public class AnimalControllerBLIntegrationTests
    {
        private AnimalController animalsController;
        private int nonExistantLang;

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            animalsController = new AnimalController();
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
            Assert.AreEqual(3, animalsController.GetAllAnimalsResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllAnimalResultsLangEn()
        {
            Assert.AreEqual(3, animalsController.GetAllAnimalsResults((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAllAnimalResultsLangAr()
        {
            Assert.AreEqual(0, animalsController.GetAllAnimalsResults((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalsLanguageNotExist()
        {
            animalsController.GetAllAnimalsResults(nonExistantLang);
        }

        #endregion

        #region GetAnimalResultsWithStory
        [TestMethod]
        public void GetAnimalResultsWithStoryLangHe()
        {
            Assert.AreEqual(2, animalsController.GetAnimalResultsWithStory((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAnimalResultsWithStoryLangEn()
        {
            Assert.AreEqual(2, animalsController.GetAnimalResultsWithStory((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAnimalResultsWithStoryLangAr()
        {
            Assert.AreEqual(0, animalsController.GetAnimalResultsWithStory((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalResultsWithStoryLanguageNotExist()
        {
            animalsController.GetAnimalResultsWithStory(nonExistantLang);
        }

        #endregion

        #region GetAnimalById
        [TestMethod]
        public void GetAnimalByIdValidInput()
        {
            var animal = animalsController.GetAnimalById(1, (int)Languages.en);
            Assert.IsInstanceOfType(animal, typeof(AnimalResult));

            Assert.AreEqual(1, animal.Id);
            Assert.AreEqual("Olive Baboon", animal.Name);
        }

        [TestMethod]
        public void GetAnimalByIdNoDataInWantedLang()
        {
            var animal = animalsController.GetAnimalById(1, (int)Languages.ar);
            Assert.IsInstanceOfType(animal, typeof(AnimalResult));

            Assert.AreEqual(1, animal.Id);
            Assert.AreEqual("בבון הזית", animal.Name);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByIdWrongId()
        {
            animalsController.GetAnimalById(-4, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByIdLanguageNotExist()
        {
            animalsController.GetAnimalById(2, nonExistantLang);
        }

        #endregion

        #region GetAnimalResultByEnclosure
        [TestMethod]
        public void GetAnimalResultByEnclosureValidInput()
        {
            var animals = animalsController.GetAnimalResultByEnclosure(1, (int)Languages.he);
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        public void GetAnimalResultByEnclosureNoDataInWantedLangauge()
        {
            var animals = animalsController.GetAnimalResultByEnclosure(1, (int)Languages.ar);
            Assert.AreEqual(2, animals.Count());

            var an = animals.First();
            Assert.AreEqual((int)Languages.he, an.Language);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalResultByEnclosureEncIdDoesntExists()
        {
            animalsController.GetAnimalResultByEnclosure(-4, -4);
        }

        #endregion

        #region GetAnimalByName
        [TestMethod]
        public void GetAnimalByNameValidInputFullName()
        {
            var animals = animalsController.GetAnimalByName("בבון הזית", (int)Languages.he);
            Assert.AreEqual(1, animals.Count());

            var olive = animals.SingleOrDefault(a => a.Name == "בבון הזית");
            Assert.AreEqual(olive.Id, 1);
        }

        [TestMethod]
        public void GetAnimalByNameValidInputPartName()
        {
            var animals = animalsController.GetAnimalByName("on", (int)Languages.en);
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
            var animals = animalsController.GetAnimalByName("on", (int)Languages.he);
            Assert.AreEqual(0, animals.Count());
        }

        [TestMethod]
        public void GetAnimalByNameAnimalNameDoesntexists()
        {
            var animals = animalsController.GetAnimalByName("abcdefghijklmnop", (int)Languages.en);
            Assert.AreEqual(0, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByNameLanguageNotExist()
        {
            animalsController.GetAnimalByName("Monkey", nonExistantLang);
        }
        #endregion

        #region GetAllAnimal
        [TestMethod]
        public void GetAllAnimal()
        {
            var animals = animalsController.GetAllAnimals();
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
            var animals = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, animals.Count());

            var an = animals.First();
            Assert.AreEqual(1, an.animalId);
            Assert.AreEqual("קופים", an.category);

            an = animals.Last();
            Assert.AreEqual(1, an.animalId);
            Assert.AreEqual("Monkies", an.category);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalDetailsByIdWrongId()
        {
            animalsController.GetAllAnimalsDetailsById(-4);
        }

        #endregion

        #region GetAnimalByEnclosure
        [TestMethod]
        public void GetAnimalByEnclosureValidInput()
        {
            var animals = animalsController.GetAnimalsByEnclosure(1);
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByEnclosureEncIdDoesntExists()
        {
            animalsController.GetAnimalsByEnclosure(-4);
        }

        #endregion

        #region UpdateAnimal
        [TestMethod]
        public void UpdateAnimalAddAnValidTest()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "הקקי שלי",
                enclosureId = 2,
            };

            animalsController.UpdateAnimal(an);

            animals = animalsController.GetAllAnimals();
            Assert.AreEqual(4, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalAddAnimalNameExists()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "קוף",
                enclosureId = 3,
            };

            animalsController.UpdateAnimal(an);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalWrongName()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "     ",
                enclosureId = 2,
            };

            animalsController.UpdateAnimal(an);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalWrongEncId()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = default(int),
                name = "הקקי שלי",
                enclosureId = -2,
            };

            animalsController.UpdateAnimal(an);
        }

        [TestMethod]
        public void UpdateAnimalValidInput()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = animals.SingleOrDefault(a => a.name == "גורילה");

            an.name = "גורילה מנייאקית";

            animalsController.UpdateAnimal(an);

            animals = animalsController.GetAllAnimals();
            Assert.IsTrue(animals.Any(a => a.name == "גורילה מנייאקית"));
            Assert.IsFalse(animals.Any(a => a.name == "גורילה"));
            Assert.AreEqual(3, animals.Count());
        }

        [TestMethod]
        public void UpdateAnimalValidButMissingInput()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = animals.SingleOrDefault(a => a.name == "גורילה");

            an.pictureUrl = "";

            animalsController.UpdateAnimal(an);

            animals = animalsController.GetAllAnimals();
            Assert.IsTrue(animals.Any(a => a.pictureUrl == ""));
            Assert.AreEqual(3, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalNameAlreadyExists()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = 2,
                name = "גורילה",
                enclosureId = 1,
            };

            an.name = "קוף";

            animalsController.UpdateAnimal(an);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalWrongId()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var an = new Animal
            {
                id = 3,
                name = "גורילה",
                enclosureId = 1
            };

            an.id = -3;

            animalsController.UpdateAnimal(an);
        }
        #endregion

        #region UpdateAnimalDetails
        [TestMethod]
        public void UpdateAnimalDetailsAddAnValidTest()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var det = new AnimalDetail
            {
                animalId = 1,
                name = "הקקי שלי",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            animalsController.UpdateAnimalDetails(det);

            details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(3, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalDetailsAddAnimalNameExists()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                name = "בבון הזית",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            animalsController.UpdateAnimalDetails(an);

        }
        
        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimaDetailsWrongName()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = 1,
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            animalsController.UpdateAnimalDetails(an);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalDetailsAddAnimalDoesntExists()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId = -1,
                name = "בבון הזית",
                category = "חרבון",
                story = "זה סיפור על קקי יפה",
                language = (long)Languages.ar
            };

            animalsController.UpdateAnimalDetails(an);

        }

        [TestMethod]
        public void UpdateAnimalDetailsValidInput()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
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
            
            animalsController.UpdateAnimalDetails(an);

            details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());
            Assert.IsTrue(details.Any(d => d.name == "אביב מאג"));
            Assert.IsFalse(details.Any(d => d.name == "בבון הזית"));

        }

        [TestMethod]
        public void UpdateAnimalDetailsValidButMissingInput()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
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

            animalsController.UpdateAnimalDetails(an);

            details = animalsController.GetAllAnimalsDetailsById(1);
            Assert.AreEqual(2, details.Count());

            an = details.SingleOrDefault(d => d.language == (int)Languages.he);
            Assert.IsNotNull(an);
            Assert.AreEqual("", an.category);
            Assert.AreEqual("", an.series);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalDetailNameAlreadyExists()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
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

            animalsController.UpdateAnimalDetails(an);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalDetailsWrongLangauge()
        {
            var details = animalsController.GetAllAnimalsDetailsById(1);
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

            animalsController.UpdateAnimalDetails(an);
        }
        #endregion
        
        #region DeleteAnimal
        [TestMethod]
        public void DeleteAnimalValidInput()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());
            
            var monk = animals.SingleOrDefault(en => en.name == "קוף");
            Assert.IsNotNull(monk);

            var details = animalsController.GetAllAnimalsDetailsById((int)monk.id);
            Assert.AreEqual(2, details.Count());

            animalsController.DeleteAnimal((int)monk.id);
            animals = animalsController.GetAllAnimals();
            Assert.AreEqual(2, animals.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteAnimalIdDoesntExists()
        {
            animalsController.DeleteAnimal(-4);
        }


        #endregion
    }
}
