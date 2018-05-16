using NegevZoo.Controllers;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DAL;
using System.Web.Http;
using BL;
using DAL.Models;
using System.Net.Http;
using System;

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
            animalsController.Request = new HttpRequestMessage();
            animalsController.Request.Headers.Add("Cookie", "session-id=123");
            animalsController.Request.RequestUri = new Uri("http://localhost:50000");
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

        #region GetAllAnimalStoryResults
        [TestMethod]
        public void GetAllAnimalStoryResultsLangHe()
        {
            Assert.AreEqual(1, animalsController.GetAllAnimalStoriesResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllAnimalStoryResultsLangEn()
        {
            Assert.AreEqual(2, animalsController.GetAllAnimalStoriesResults((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAllAnimalStoryResultsLangAr()
        {
            Assert.AreEqual(0, animalsController.GetAllAnimalStoriesResults((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalStoryLanguageNotExist()
        {
            animalsController.GetAllAnimalStoriesResults(nonExistantLang);
        }
        #endregion

        #region GetAnimalStoryResultById
        [TestMethod]
        public void GetAllAnimalStoryByIdValidInput()
        {
            var animalStory = animalsController.GetAnimalStoryResultById(1, (int)Languages.he);

            Assert.AreEqual("גילי הבבון", animalStory.Name);
            Assert.AreEqual("storyUrl1", animalStory.PictureUrl);
        }

        [TestMethod]
        public void GetAnimalStoryResultsByIdWrongLangButExisting()
        {
            var animalStory = animalsController.GetAnimalStoryResultById(1, (int)Languages.ar);

            Assert.AreEqual("גילי הבבון", animalStory.Name);
            Assert.AreEqual("storyUrl1", animalStory.PictureUrl);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalStoryByIdWrongAnimalStoryId()
        {
            animalsController.GetAnimalStoryResultById(-1, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalStoryByIdLanguageNotExist()
        {
            animalsController.GetAnimalStoryResultById(1, nonExistantLang);
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

        #region GetAllAnimalStories
        [TestMethod]
        public void GetAllAnimalStories()
        {
            var animalStories = animalsController.GetAllAnimalStories();
            Assert.AreEqual(2, animalStories.Count());

            var story = animalStories.First();

            Assert.AreEqual(1, story.id);
            Assert.AreEqual("storyUrl1", story.pictureUrl);
            Assert.AreEqual(1, story.enclosureId);
        }

        #endregion

        #region GetAllAnimalStoryDetailsById
        [TestMethod]
        public void GetAllAnimalStoryDetailsByIdValidInput()
        {
            var animalStory = animalsController.GetAllAnimalStoryDetailsById(1);
            Assert.AreEqual(2, animalStory.Count());

            var story = animalStory.First();
            Assert.AreEqual(1, story.animalStoryId);
            Assert.AreEqual("גילי הבבון", story.name);
            Assert.AreEqual(1, story.language);

            story = animalStory.Last();
            Assert.AreEqual(1, story.animalStoryId);
            Assert.AreEqual("Gili the olive baboon", story.name);
            Assert.AreEqual(2, story.language);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalStoryDetailsByIdWrongId()
        {
            animalsController.GetAllAnimalsDetailsById(-4);
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

        #region UpdateAnimalStory
        [TestMethod]
        public void UpdateAnimalStoryAddAnValidTest()
        {
            var animalStory = animalsController.GetAllAnimalStories();
            Assert.AreEqual(2, animalStory.Count());

            var ans = new AnimalStory
            {
                id = default(int),
                enclosureId = 2,
                pictureUrl = "storyUrl1000"
            };

            animalsController.UpdateAnimalStory(ans);

            animalStory = animalsController.GetAllAnimalStories();
            Assert.AreEqual(3, animalStory.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryWrongEnclosureId()
        {
            var ans = new AnimalStory
            {
                id = default(int),
                pictureUrl = "pictureUrl1",
                enclosureId = -2,
            };

            animalsController.UpdateAnimalStory(ans);
        }
        
        [TestMethod]
        public void UpdateAnimalStoryValidInput()
        {
            var animalStories = animalsController.GetAllAnimalStories();
            Assert.AreEqual(2, animalStories.Count());
            Assert.IsFalse(animalStories.Any(a => a.enclosureId == 2));

            var ans = animalStories.SingleOrDefault(a => a.id == 1);

            ans.enclosureId = 2;

            animalsController.UpdateAnimalStory(ans);

            animalStories = animalsController.GetAllAnimalStories();
            Assert.IsTrue(animalStories.Any(a => a.enclosureId == 2));
            Assert.AreEqual(2, animalStories.Count());
        }
        #endregion
        
        #region UpdateAnimalStoryDetail
        [TestMethod]
        public void UpdateAnimalStoryDetailsAddStoryValidTest()
        {
            var details = animalsController.GetAllAnimalStoryDetailsById(2);
            Assert.AreEqual(1, details.Count());

            var det = new AnimalStoryDetail
            {
                animalStoryId = 2,
                name = "שוש הזברה",
                story = "לשוש הזברה ספור מיוחד מאוד.",
                language = (int)Languages.he
            };

            animalsController.UpdateAnimalStoryDetail(det);

            details = animalsController.GetAllAnimalStoryDetailsById(2);
            Assert.AreEqual(2, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsWrongName()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "    ",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsWrongStory()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "שם"
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
        }
        
        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsAddAnimalStoryDoesntExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = -1,
                name = "שם",
                story = "סיפור",
                language = (int)Languages.ar
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsAddAnimalNameExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = default(int),
                name = "גילי בבון הזית",
                story = "סיפור",
                language = (int)Languages.he
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        public void UpdateAnimalStoryDetailsValidInput()
        {
            var details = animalsController.GetAllAnimalStoryDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "גילי הבבון",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            ansd.name = "אביב מאג";

            animalsController.UpdateAnimalStoryDetail(ansd);

            details = animalsController.GetAllAnimalStoryDetailsById(1);
            Assert.AreEqual(2, details.Count());
            Assert.IsTrue(details.Any(d => d.name == "אביב מאג"));
            Assert.IsFalse(details.Any(d => d.name == "גילי הבבון"));

        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsNameExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.en,
                name = "Shosh the Zebra",
                story = "Gili the baboon have a very speacial story"
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
        }
        
        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAnimalStoryDetailsWrongLangauge()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = -100,
                name = "גילי הבבון",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            animalsController.UpdateAnimalStoryDetail(ansd);
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

        #region DeleteAnimalStory
        [TestMethod]
        public void DeleteAnimalStoryValidInput()
        {
            var animalStories = animalsController.GetAllAnimalStories();
            Assert.AreEqual(2, animalStories.Count());

            var giliStory = animalStories.SingleOrDefault(en => en.id == 1);
            Assert.IsNotNull(giliStory);

            var details = animalsController.GetAllAnimalStoryDetailsById((int)giliStory.id);
            Assert.AreEqual(2, details.Count());

            animalsController.DeleteAnimalStory((int)giliStory.id);
            animalStories = animalsController.GetAllAnimalStories();
            Assert.AreEqual(1, animalStories.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteAnimalStoryIdDoesntExists()
        {
            animalsController.DeleteAnimal(-4);
        }
        #endregion
    }
}
