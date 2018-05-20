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

        #region GetAllAnimalStoryResults
        [TestMethod]
        public void GetAllAnimalStoryResultsLangHe()
        {
            Assert.AreEqual(1, context.GetAllAnimalStoriesResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllAnimalStoryResultsLangEn()
        {
            Assert.AreEqual(2, context.GetAllAnimalStoriesResults((int)Languages.en).Count());
        }

        [TestMethod]
        public void GetAllAnimalStoryResultsLangAr()
        {
            Assert.AreEqual(0, context.GetAllAnimalStoriesResults((int)Languages.ar).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAllAnimalStoryLanguageNotExist()
        {
            context.GetAllAnimalStoriesResults(nonExistantLang);
        }
        #endregion

        #region GetAnimalStoryResultById
        [TestMethod]
        public void GetAllAnimalStoryByIdValidInput()
        {
            var animalStory = context.GetAnimalStoryResultById(1, (int)Languages.he);

            Assert.AreEqual("גילי הבבון", animalStory.Name);
            Assert.AreEqual("storyUrl1", animalStory.PictureUrl);
        }

        [TestMethod]
        public void GetAnimalStoryResultsByIdWrongLangButExisting()
        {
            var animalStory = context.GetAnimalStoryResultById(1, (int)Languages.ar);

            Assert.AreEqual("גילי הבבון", animalStory.Name);
            Assert.AreEqual("storyUrl1", animalStory.PictureUrl);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. AnimalStory id doesn't exsits")]
        public void GetAnimalStoryByIdWrongAnimalStoryId()
        {
            context.GetAnimalStoryResultById(-1, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input.Wrong language.")]
        public void GetAnimalStoryByIdLanguageNotExist()
        {
            context.GetAnimalStoryResultById(1, nonExistantLang);
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

        #region GetAllAnimalStories
        [TestMethod]
        public void GetAllAnimalStories()
        {
            var animalStories = context.GetAllAnimalStories();
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
            var animalStory = context.GetAllAnimalStoryDetailsById(1);
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
        [ExpectedException(typeof(ArgumentException), "Wrong input. The AnimalStory id doesn't exists")]
        public void GetAllAnimalStoryDetailsByIdWrongId()
        {
            context.GetAllAnimalStoryDetailsById(-4);
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
                animalId        = 1,
                name            = "הקקי שלי",
                category        = "חרבון",
                interesting     = "זה סיפור על קקי יפה",
                language        = (long)Languages.ar
            };

            context.UpdateAnimalDetails(det);

            details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(3, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The name is null or whitespace.")]
        public void UpdateAnimaDetailsWrongName()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId    = 1,
                category    = "חרבון",
                interesting = "זה סיפור על קקי יפה",
                language    = (long)Languages.ar
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
                animalId        = -1,
                name            = "בבון הזית",
                category        = "חרבון",
                interesting     = "זה סיפור על קקי יפה",
                language        = (long)Languages.ar
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
                animalId        = 1,
                language        = (long)Languages.he,
                name            = "בבון הזית",
                category        = "קופים",
                series          = "קוף",
                distribution    = "",
                family          = "",
                food            = "",
                reproduction    = "",
                interesting     = "גילאור בבון הזית מאוד חמוד"
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
                animalId        = 1,
                language        = (long)Languages.he,
                name            = "בבון הזית",
                category        = "קופים",
                series          = "קוף",
                distribution    = "",
                family          = "",
                food            = "",
                reproduction    = "",
                interesting     = "גילאור בבון הזית מאוד חמוד"
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
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void UpdateAnimalDetailsWrongLangauge()
        {
            var details = context.GetAllAnimalsDetailById(1);
            Assert.AreEqual(2, details.Count());

            var an = new AnimalDetail
            {
                animalId        = 1,
                language        = -4,
                name            = "בבון הזית",
                category        = "קופים",
                series          = "קוף",
                distribution    = "",
                family          = "",
                food            = "",
                reproduction    = "",
                interesting     = "גילאור בבון הזית מאוד חמוד"
            };
            an.name = "גורילה";

            context.UpdateAnimalDetails(an);
        }
        #endregion

        #region UpdateAnimalStory
        [TestMethod]
        public void UpdateAnimalStoryAddAnValidTest()
        {
            var animalStory = context.GetAllAnimalStories();
            Assert.AreEqual(2, animalStory.Count());

            var ans = new AnimalStory
            {
                id = default(int),
                enclosureId = 2,
                pictureUrl = "storyUrl1000"
            };

            context.UpdateAnimalStory(ans);

            animalStory = context.GetAllAnimalStories();
            Assert.AreEqual(3, animalStory.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure id doesn't exists")]
        public void UpdateAnimalStoryWrongEnclosureId()
        {
            var ans = new AnimalStory
            {
                id = default(int),
                pictureUrl = "pictureUrl1",
                enclosureId = -2,
            };

            context.UpdateAnimalStory(ans);
        }

        [TestMethod]
        public void UpdateAnimalStoryValidInput()
        {
            var animalStories = context.GetAllAnimalStories();
            Assert.AreEqual(2, animalStories.Count());
            Assert.IsFalse(animalStories.Any(a => a.enclosureId == 2));

            var ans = animalStories.SingleOrDefault(a => a.id == 1);

            ans.enclosureId = 2;

            context.UpdateAnimalStory(ans);

            animalStories = context.GetAllAnimalStories();
            Assert.IsTrue(animalStories.Any(a => a.enclosureId == 2));
            Assert.AreEqual(2, animalStories.Count());
        }
        #endregion

        #region UpdateAnimalStoryDetail
        [TestMethod]
        public void UpdateAnimalStoryDetailsAddStoryValidTest()
        {
            var details = context.GetAllAnimalStoryDetailsById(2);
            Assert.AreEqual(1, details.Count());

            var det = new AnimalStoryDetail
            {
                animalStoryId = 2,
                name = "שוש הזברה",
                story = "לשוש הזברה ספור מיוחד מאוד.",
                language = (int)Languages.he
            };

            context.UpdateAnimalStoryDetail(det);

            details = context.GetAllAnimalStoryDetailsById(2);
            Assert.AreEqual(2, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The name is null or white spaces")]
        public void UpdateAnimalStoryDetailsWrongName()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "    ",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            context.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The story is null or white spaces.")]
        public void UpdateAnimalStoryDetailsWrongStory()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "שם"
            };

            context.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The animal story id doesn't exists")]
        public void UpdateAnimalStoryDetailsAddAnimalStoryDoesntExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = -1,
                name = "שם",
                story = "סיפור",
                language = (int)Languages.ar
            };

            context.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding AnimalStoryResult. The name already exits")]
        public void UpdateAnimalStoryDetailsAddAnimalNameExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = default(int),
                name = "גילי בבון הזית",
                story = "סיפור",
                language = (int)Languages.he
            };

            context.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        public void UpdateAnimalStoryDetailsValidInput()
        {
            var details = context.GetAllAnimalStoryDetailsById(1);
            Assert.AreEqual(2, details.Count());

            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.he,
                name = "גילי הבבון",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            ansd.name = "אביב מאג";

            context.UpdateAnimalStoryDetail(ansd);

            details = context.GetAllAnimalStoryDetailsById(1);
            Assert.AreEqual(2, details.Count());
            Assert.IsTrue(details.Any(d => d.name == "אביב מאג"));
            Assert.IsFalse(details.Any(d => d.name == "גילי הבבון"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating AnimalStoryResult. The name already exits")]
        public void UpdateAnimalStoryDetailsNameExists()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = (int)Languages.en,
                name = "Shosh the Zebra",
                story = "Gili the baboon have a very speacial story"
            };

            context.UpdateAnimalStoryDetail(ansd);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void UpdateAnimalStoryDetailsWrongLangauge()
        {
            var ansd = new AnimalStoryDetail
            {
                animalStoryId = 1,
                language = -100,
                name = "גילי הבבון",
                story = "לגילי הבבון סיפור מיוחד ומרגש"
            };

            context.UpdateAnimalStoryDetail(ansd);
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

        #region DeleteAnimalStory
        [TestMethod]
        public void DeleteAnimalStoryValidInput()
        {
            var animalStories = context.GetAllAnimalStories();
            Assert.AreEqual(2, animalStories.Count());

            var giliStory = animalStories.SingleOrDefault(en => en.id == 1);
            Assert.IsNotNull(giliStory);

            var details = context.GetAllAnimalStoryDetailsById((int)giliStory.id);
            Assert.AreEqual(2, details.Count());

            context.DeleteAnimalStory((int)giliStory.id);
            animalStories = context.GetAllAnimalStories();
            Assert.AreEqual(1, animalStories.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. AnimalStory doesn't exists")]
        public void DeleteAnimalStoryIdDoesntExists()
        {
            context.DeleteAnimal(-4);
        }
        #endregion
    }
}
