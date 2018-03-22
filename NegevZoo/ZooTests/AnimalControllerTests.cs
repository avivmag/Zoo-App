//using NegevZoo.Controllers;
//using System.Linq;
//using Microsoft.VisualStudio.TestTools.UnitTesting;
//using DAL;
//using System.Web.Http;
//using BL;

//namespace ZooTests
//{
//    [TestClass]
//    public class AnimalControllerTests
//    {
//        private  AnimalController animalsController;
//        private int nonExistantLang;

//        #region SetUp and TearDown
//        [TestInitialize]
//        public void SetUp()
//        {
//            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
//            ControllerBase.isTesting = true;
//            animalsController = new AnimalController();
//            nonExistantLang = 10000;
//        }

//        [TestCleanup]
//        public void EnclosureCleanUp()
//        {
//            //ZooContext.CleanDb();
//        }

//        #endregion

//        #region GetAllAnimals
//        [TestMethod]
//        public void GetAllAnimalsLangHe()
//        {
//            Assert.AreEqual(3, animalsController.GetAllAnimals((int)Languages.he).Count());
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAllAnimalsLanguageNotExist()
//        {
//            animalsController.GetAllAnimals(nonExistantLang);
//        }

//        #endregion

//        #region GetAnimalById
//        [TestMethod]
//        public void GetAnimalByIdValidInput()
//        {
//            var animal = animalsController.GetAnimalById(1, (int)Languages.en);
//            Assert.IsInstanceOfType(animal, typeof(Animal));
//            Animal animalObject = (Animal)animal;
            
//            Assert.AreEqual(1, animalObject.id);
//            Assert.AreEqual("Olive Baboon", animalObject.name);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByIdWrongId()
//        {
//            animalsController.GetAnimalById(-4, (int)Languages.he);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByIdLanguageNotExist()
//        {
//            animalsController.GetAnimalById(4, nonExistantLang);
//        }

//        #endregion

//        #region getAnimalByName
//        [TestMethod]
//        public void GetAnimalByNameValidInputFullName()
//        {
//            var animals = animalsController.GetAnimalByName("Olive Baboon", (int)Languages.en);
//            Assert.AreEqual(1, animals.Count());

//            var olive = animals.SingleOrDefault(a => a.name == "Olive Baboon");
//            Assert.AreEqual(olive.id, 1);
//        }

//        [TestMethod]
//        public void GetAnimalByNameValidInputPartName()
//        {
//            var animals = animalsController.GetAnimalByName("on", (int)Languages.en);
//            Assert.AreEqual(2, animals.Count());

//            var olive = animals.SingleOrDefault(a => a.name == "Olive Baboon");
//            Assert.AreEqual(olive.id, 1);

//            var monkey = animals.SingleOrDefault(a => a.name == "Monkey");
//            Assert.AreEqual(monkey.id, 5);

//            var gorila = animals.SingleOrDefault(a => a.name == "Gorilla");
//            Assert.IsNull(gorila);
//        }

//        [TestMethod]
//        public void GetAnimalByNameValidNameWrongLang()
//        {
//            var animals = animalsController.GetAnimalByName("on", (int)Languages.he);
//            Assert.AreEqual(0, animals.Count());
//        }

//        [TestMethod]
//        public void GetAnimalByNameAnimalNameDoesntexists()
//        {
//            var animals = animalsController.GetAnimalByName("abcdefghijklmnop", (int)Languages.en);
//            Assert.AreEqual(0, animals.Count());
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByNameLanguageNotExist()
//        {
//            animalsController.GetAnimalByName("Monkey", nonExistantLang);
//        }
//        #endregion

//        #region GetAnimalByEnclosure
//        [TestMethod]
//        public void GetAnimalByEnclosureValidInput()
//        {
//            var animals = animalsController.GetAnimalsByEnclosure(2, (int)Languages.he);
//            Assert.AreEqual(2, animals.Count());
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByEnclosureValidEncIdWrongLanguage()
//        {
//            animalsController.GetAnimalsByEnclosure(2, (int)Languages.en);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByEnclosureValidInputWrongLanguage()
//        {
//            animalsController.GetAnimalsByEnclosure(2, (int)Languages.en);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByEnclosureEncIdDoesntExists()
//        {
//            animalsController.GetAnimalsByEnclosure(-4, (int)Languages.en);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void GetAnimalByEnclosureWrongLanguage()
//        {
//            animalsController.GetAnimalsByEnclosure(2, nonExistantLang);
//        }
//        #endregion

//        #region UpdateAnimal
//        [TestMethod]
//        public void UpdateAnimalAddAnValidTest()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "הקקי שלי",
//                enclosureId = 2,
//                //language = (int)Languages.he,
//                //story = "הקקי שלי הוא גדול ומוצק"
//            };

//            animalsController.UpdateAnimal(an);

//            animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(4, animals.Count());
//        }

//        [TestMethod]
//        public void UpdateAnimalAddAnValidTestButMissing()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "הקקי שלי",
//                enclosureId = 2,
//                //language = (int)Languages.he,
//                //story = ""
//            };

//            animalsController.UpdateAnimal(an);

//            animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(4, animals.Count());

//            var newAn = animals.SingleOrDefault(a => a.name == an.name);
//            //Assert.AreEqual("", newAn.story);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalWrongLanguage()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "הקקי שלי",
//                enclosureId = 2,
//                //language = nonExistantLang,
//                //story = ""
//            };

//            animalsController.UpdateAnimal(an);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalWrongName()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "     ",
//                enclosureId = 2,
//                //language = (int)Languages.he,
//                //story = ""
//            };

//            animalsController.UpdateAnimal(an);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalWrongEncId()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "הקקי שלי",
//                enclosureId = -2,
//                //language = (int)Languages.he,
//                //story = ""
//            };

//            animalsController.UpdateAnimal(an);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalAddAnimalNameExists()
//        {
//            var animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = default(int),
//                name = "Monkey",
//                enclosureId = 3,
//                //language = (int)Languages.en,
//                //story = "This is new monkey"
//            };

//            animalsController.UpdateAnimal(an);
//        }

//        [TestMethod]
//        public void UpdateAnimalValidInput()
//        {
//            var animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.AreEqual(3, animals.Count());

//            var an = animals.SingleOrDefault(a => a.name == "Gorilla");

//            an.name = "Mother fucking Gorilla";

//            animalsController.UpdateAnimal(an);

//            animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.IsTrue(animals.Any(a => a.name == "Mother fucking Gorilla"));
//            Assert.AreEqual(3, animals.Count());
//        }

//        [TestMethod]
//        public void UpdateAnimalValidButMissingInput()
//        {
//            var animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.AreEqual(3, animals.Count());

//            var an = animals.SingleOrDefault(a => a.name == "Gorilla");
            
//            //an.story = "";

//            animalsController.UpdateAnimal(an);

//            animals = animalsController.GetAllAnimals((int)Languages.en);
//            //Assert.IsTrue(animals.Any(a => a.story == ""));
//            Assert.AreEqual(3, animals.Count());
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalNameAlreadyExists()
//        {
//            var animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = 3,
//                name = "Gorilla",
//                enclosureId = 1,
//                //story = "Shrek the mighty gorilla!",
//                //language = (int)Languages.en
//            };

//            an.name = "Monkey";

//            animalsController.UpdateAnimal(an);
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void UpdateAnimalWrongId()
//        {
//            var animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.AreEqual(3, animals.Count());

//            var an = new Animal
//            {
//                id = 3,
//                name = "Gorilla",
//                enclosureId = 1,
//                //story = "Shrek the mighty gorilla!",
//                //language = (int)Languages.en
//            };

//            an.id = -3;

//            animalsController.UpdateAnimal(an);
//        }
//        #endregion

//        #region DeleteAnimal
//        [TestMethod]
//        public void DeleteAnimalValidInput()
//        {
//            var animals = animalsController.GetAllAnimals((int)Languages.en);
//            Assert.AreEqual(3, animals.Count());

//            var monk = animals.SingleOrDefault(en => en.name == "Monkey");
//            Assert.IsNotNull(monk);

//            animalsController.DeleteAnimal((int)monk.id);
//            animals = animalsController.GetAllAnimals((int)Languages.en);

//            Assert.AreEqual(2, animals.Count());

//            animals = animalsController.GetAllAnimals();
//            Assert.AreEqual(3, animals.Count());
//        }

//        [TestMethod]
//        [ExpectedException(typeof(HttpResponseException))]
//        public void DeleteAnimalIdDoesntExists()
//        {
//            animalsController.DeleteAnimal(-4);
//        }

//        #endregion
//    }
//}
