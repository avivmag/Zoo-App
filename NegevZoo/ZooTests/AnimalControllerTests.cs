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
    public class AnimalControllerTests
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
        public void GetAllAnimalsLangHe()
        {
            Assert.AreEqual(3, animalsController.GetAllAnimalsResults((int)Languages.he).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalsLanguageNotExist()
        {
            animalsController.GetAllAnimalsResults(nonExistantLang);
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

        #region getAnimalByName
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

        #region GetAnimalByEnclosure
        [TestMethod]
        public void GetAnimalByEnclosureValidInput()
        {
            var animals = animalsController.GetAnimalsByEnclosure(1, (int)Languages.he);
            Assert.AreEqual(2, animals.Count());
        }
        
        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByEnclosureEncIdDoesntExists()
        {
            animalsController.GetAnimalsByEnclosure(-4, (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByEnclosureWrongLanguage()
        {
            animalsController.GetAnimalsByEnclosure(2, nonExistantLang);
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
                //language = (int)Languages.he,
                //story = "הקקי שלי הוא גדול ומוצק"
            };

            animalsController.UpdateAnimal(an);

            animals = animalsController.GetAllAnimals();
            Assert.AreEqual(4, animals.Count());
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
                //language = (int)Languages.he,
                //story = ""
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
                //language = (int)Languages.he,
                //story = ""
            };

            animalsController.UpdateAnimal(an);
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
                //language = (int)Languages.en,
                //story = "This is new monkey"
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
                //story = "Shrek the mighty gorilla!",
                //language = (int)Languages.en
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

        #region DeleteAnimal
        [TestMethod]
        public void DeleteAnimalValidInput()
        {
            var animals = animalsController.GetAllAnimals();
            Assert.AreEqual(3, animals.Count());

            var monk = animals.SingleOrDefault(en => en.name == "קוף");
            Assert.IsNotNull(monk);

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
        [TestMethod]
        public void GetAnimalWithStory()
        {
            var allAnimals = animalsController.GetAllAnimalsResults();
            Assert.AreEqual(3, allAnimals.Count());

            var animalsWithStory = animalsController.GetAnimalsWithStoryResults();
            Assert.AreEqual(2, animalsWithStory.Count());
        }
    }
}
