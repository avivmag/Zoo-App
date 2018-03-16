using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Backend;
using System.Collections.Generic;
using System.Web.Http;
using BL;

namespace ZooTests
{
    [TestClass]
    public class EncloureControllerTests
    {
        private EnclosureController enclosureController;
        private int nonExistantLang;

        #region tests SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            enclosureController = new EnclosureController();
            nonExistantLang = 100;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            ZooContext.CleanDb();
        }

        #endregion

        #region GetAllEnclosures()
        [TestMethod]
        public void GetAllEnclosuresLangHe()
        {
            Assert.AreEqual(3, enclosureController.GetAllEnclosures((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllEnclosuresLangEng()
        {
            Assert.AreEqual(3, enclosureController.GetAllEnclosures((int)Languages.en).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllEnclosuresLangNotExist()
        {
            enclosureController.GetAllEnclosures(nonExistantLang);
        }
        #endregion

        #region GetEnclosureById
        [TestMethod]
        public void GetEnclosuresByValidId()
        {
            var enc = enclosureController.GetEnclosureById(2);

            //default in hebrew
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            //english
            enc = enclosureController.GetEnclosureById(1, 2);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("Monkeys", enc.Name);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByIdWrongId()
        {
            enclosureController.GetEnclosureById(400);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByIdWrongLanguage()
        {
            enclosureController.GetEnclosureById(1, nonExistantLang);
        }
        #endregion

        #region GetEnclosureByName
        [TestMethod]
        public void GetEnclosuresByNameValidInput()
        {
            IEnumerable<Enclosure> encs = enclosureController.GetEnclosureByName("תצוגת הקופים", 1);

            Assert.AreEqual(1, encs.Count());

            Enclosure enc = encs.ElementAt(0);

            //default in hebrew
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            //english
            encs = enclosureController.GetEnclosureByName("Houman Monkeys", 2);

            Assert.AreEqual(1, encs.Count());

            enc = encs.ElementAt(0);

            Assert.IsNotNull(enc);
            Assert.AreEqual(3, enc.Id);
            Assert.AreEqual("Houman Monkeys", enc.Name);
        }

        [TestMethod]
        public void GetEnclosuresByNamePartialyName()
        {
            IEnumerable<Enclosure> encs = enclosureController.GetEnclosureByName("Monkeys", 2);

            Assert.AreEqual(2, encs.Count());
            Assert.AreEqual("Monkeys", encs.ElementAt(0).Name);
            Assert.AreEqual("Houman Monkeys", encs.ElementAt(1).Name);
        }

        [TestMethod]
        public void GetEnclosuresByNameWrongName()
        {
            var encs = enclosureController.GetEnclosureByName("שימפנזות");

            Assert.IsNotNull(encs);
            Assert.AreEqual(0, encs.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByNameWrongLanguage()
        {
            enclosureController.GetEnclosureByName("Houman Monkeys", nonExistantLang);
        }

        [TestMethod]
        public void GetEnclosuresByNameValidNameWrongLang()
        {
            var encs = enclosureController.GetEnclosureByName("Houman Monkeys");

            Assert.IsNotNull(encs);
            Assert.AreEqual(0, encs.Count());
        }
        #endregion

        //TODO: Add tests after implementaion.
        #region GetEnclsoureByPosition

        #endregion

        #region GetAllRecurringEvents
        [TestMethod]
        public void GetAllRecutnigEventsValidInput()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = encs.ElementAt(0);
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            var recurringEvents = enclosureController.GetRecurringEvents(enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(1, recurringEvents.Count());

            var recEvent = recurringEvents.ElementAt(0);
            Assert.IsNotNull(recEvent);
            Assert.AreEqual(2, recEvent.Id);
            Assert.AreEqual(enc.Id, recEvent.Id);
            Assert.AreEqual(enc.Language, recEvent.Language);
            Assert.AreEqual(10, recEvent.StartHour);
            Assert.AreEqual(30, recEvent.StartMin);
        }

        [TestMethod]
        public void GetRecurringEventValidInputEmptyList()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = encs.SingleOrDefault(en => en.Name == "זברה");
            Assert.IsNotNull(enc);
            Assert.AreEqual(6, enc.Id);

            var recurringEvents = enclosureController.GetRecurringEvents(enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(0, recurringEvents.Count());
        }

        [TestMethod]
        public void GetRecurringEventsValidIdWrongLanguage()
        {
            var recEvents = enclosureController.GetRecurringEvents(1);

            Assert.IsNotNull(recEvents);
            Assert.AreEqual(0, recEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetRecurringEventsWrongId()
        {
            var recEvents = enclosureController.GetRecurringEvents(200);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetRecurringEventsWrongLanguage()
        {
            enclosureController.GetRecurringEvents(nonExistantLang);
        }
        #endregion

        #region DeleteRecurringEvent
        [TestMethod]
        public void DeleteRecurringEventValidInput()
        {
            var enc = enclosureController.GetAllEnclosures().SingleOrDefault(e => e.Name == "קופי אדם");
            Assert.IsNotNull(enc);

            var allRecEve = enclosureController.GetRecurringEvents(enc.Id);
            Assert.AreEqual(2, allRecEve.Count());

            var eve = allRecEve.SingleOrDefault(e => e.Id == 4);
            Assert.AreEqual(eve.Day, "ראשון");
            Assert.AreEqual(eve.Descroption, "משחק");

            enclosureController.DeleteRecurringEvent(eve.Id);

            allRecEve = enclosureController.GetRecurringEvents(enc.Id);
            Assert.AreEqual(1, allRecEve.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteRecuringEventIdDoesntExists()
        {
            enclosureController.DeleteRecurringEvent(1000);
        }
        #endregion

        #region UpdateEnclosure
        [TestMethod]
        public void UpdateEnclosureAddEncValidTest()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                Id = default(int),
                Name = "Lions enclosure",
                Story = "Finalt they are here",
                Language = (int)Languages.en
            };

            enclosureController.UpdateEnclosure(enc);

            encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.AreEqual(4, encs.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureAddTestExistingName()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                Id = default(int),
                Name = "Monkeys",
                Story = "Finalt they are here",
                Language = (int)Languages.en
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureAddTestEmptyName()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                Id = default(int),
                Name = "",
                Story = "Finalt they are here",
                Language = (int)Languages.en
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureAddTestWrongLang()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                Id = default(int),
                Name = "Lions enclosure",
                Story = "Finalt they are here",
                Language = nonExistantLang
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        public void UpdateEnclosureValidTest()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            monkeyEncEn.Name = "kaki";
            enclosureController.UpdateEnclosure(monkeyEncEn);

            encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.AreEqual(3, encs.Count());

            var updatedEnc = encs.SingleOrDefault(en => en.Name == "kaki");
            Assert.IsNotNull(updatedEnc);

            var nothing = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNull(nothing);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureExistingName()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                Id = monkeyEncEn.Id,
                Language = monkeyEncEn.Language,
                Latitude = monkeyEncEn.Latitude,
                Longtitude = monkeyEncEn.Longtitude,
                Name = "Houman Monkeys"
            };
            
            enclosureController.UpdateEnclosure(human);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureEmptyName()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                Id = monkeyEncEn.Id,
                Language = monkeyEncEn.Language,
                Latitude = monkeyEncEn.Latitude,
                Longtitude = monkeyEncEn.Longtitude,
                Name = ""
            };

            enclosureController.UpdateEnclosure(human);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureDoesntExists()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                Id = 1000000,
                Language = monkeyEncEn.Language,
                Latitude = monkeyEncEn.Latitude,
                Longtitude = monkeyEncEn.Longtitude,
                Name = monkeyEncEn.Name
            };

            enclosureController.UpdateEnclosure(human);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureWrongLang()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.Name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                Id = monkeyEncEn.Id,
                Language = nonExistantLang,
                Latitude = monkeyEncEn.Latitude,
                Longtitude = monkeyEncEn.Longtitude,
                Name = monkeyEncEn.Name
            };

            enclosureController.UpdateEnclosure(human);
        }
        #endregion

        #region DeleteEnclosure
        [TestMethod]
        public void DeleteEnclosureValidInput()
        {
            var encsHeb = enclosureController.GetAllEnclosures();
            Assert.AreEqual(3, encsHeb.Count());

            var monkHeb = encsHeb.SingleOrDefault(en => en.Name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            //delete animals
            AnimalController anCont = new AnimalController();
            var animalsList = anCont.GetAnimalsByEnclosure(monkHeb.Id, monkHeb.Language);

            foreach (Animal an in animalsList)
            {
                anCont.DeleteAnimal(an.Id);
            }

            //delete reccuringEvents
            var recEvents = enclosureController.GetRecurringEvents(monkHeb.Id);

            foreach (RecurringEvent eve in recEvents)
            {
                enclosureController.DeleteRecurringEvent(eve.Id);
            }

            enclosureController.DeleteEnclosure(monkHeb.Id);
            encsHeb = enclosureController.GetAllEnclosures();

            Assert.AreEqual(2, encsHeb.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteEnclosureAnimalsExists()
        {
            var encsHeb = enclosureController.GetAllEnclosures();
            Assert.IsNotNull(encsHeb);
            Assert.AreEqual(3, encsHeb.Count());

            var monkHeb = encsHeb.SingleOrDefault(en => en.Name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            enclosureController.DeleteEnclosure(monkHeb.Id);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteEnclosureRecurringEventsExists()
        {
            var encsHeb = enclosureController.GetAllEnclosures();
            Assert.IsNotNull(encsHeb);
            Assert.AreEqual(3, encsHeb.Count());

            var monkHeb = encsHeb.SingleOrDefault(en => en.Name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            //delete animals
            AnimalController anCont = new AnimalController();
            var animalsList = anCont.GetAnimalsByEnclosure(monkHeb.Id, monkHeb.Language);

            foreach (Animal an in animalsList)
            {
                anCont.DeleteAnimal(an.Id);
            }

            enclosureController.DeleteEnclosure(monkHeb.Id);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteEnclosureEnclosureDoesntExists()
        {
            enclosureController.DeleteEnclosure(1000);
        }
        #endregion
    }
}
