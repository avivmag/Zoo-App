using NegevZoo.Controllers;
using DAL;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
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
            Assert.AreEqual(2, enc.id);
            Assert.AreEqual("תצוגת הקופים", enc.name);

            //english
            enc = enclosureController.GetEnclosureById(1, 2);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.id);
            Assert.AreEqual("Monkeys", enc.name);
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
            Assert.AreEqual(2, enc.id);
            Assert.AreEqual("תצוגת הקופים", enc.name);

            //english
            encs = enclosureController.GetEnclosureByName("Houman Monkeys", 2);

            Assert.AreEqual(1, encs.Count());

            enc = encs.ElementAt(0);

            Assert.IsNotNull(enc);
            Assert.AreEqual(3, enc.id);
            Assert.AreEqual("Houman Monkeys", enc.name);
        }

        [TestMethod]
        public void GetEnclosuresByNamePartialyName()
        {
            IEnumerable<Enclosure> encs = enclosureController.GetEnclosureByName("Monkeys", 2);

            Assert.AreEqual(2, encs.Count());
            Assert.AreEqual("Monkeys", encs.ElementAt(0).name);
            Assert.AreEqual("Houman Monkeys", encs.ElementAt(1).name);
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
            Assert.AreEqual(2, enc.id);
            Assert.AreEqual("תצוגת הקופים", enc.name);

            var recurringEvents = enclosureController.GetRecurringEvents((int)enc.id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(1, recurringEvents.Count());

            var recEvent = recurringEvents.ElementAt(0);
            Assert.IsNotNull(recEvent);
            Assert.AreEqual(2, recEvent.id);
            Assert.AreEqual(enc.id, recEvent.id);
            Assert.AreEqual(enc.language, recEvent.language);
            Assert.AreEqual(10, recEvent.startHour);
            Assert.AreEqual(30, recEvent.startMin);
        }

        [TestMethod]
        public void GetRecurringEventValidInputEmptyList()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = encs.SingleOrDefault(en => en.name == "זברה");
            Assert.IsNotNull(enc);
            Assert.AreEqual(6, enc.id);

            var recurringEvents = enclosureController.GetRecurringEvents((int)enc.id, (int)Languages.he);
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
            var enc = enclosureController.GetAllEnclosures().SingleOrDefault(e => e.name == "קופי אדם");
            Assert.IsNotNull(enc);

            var allRecEve = enclosureController.GetRecurringEvents((int)enc.id);
            Assert.AreEqual(2, allRecEve.Count());

            var eve = allRecEve.SingleOrDefault(e => e.id == 4);
            Assert.AreEqual(eve.day, "ראשון");
            Assert.AreEqual(eve.description, "משחק");

            enclosureController.DeleteRecurringEvent((int)eve.id);

            allRecEve = enclosureController.GetRecurringEvents((int)enc.id);
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
                id = default(int),
                name = "Lions enclosure",
                story = "Finalt they are here",
                language = (int)Languages.en
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
                id = default(int),
                name = "Monkeys",
                story = "Finalt they are here",
                language = (int)Languages.en
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
                id = default(int),
                name = "",
                story = "Finalt they are here",
                language = (int)Languages.en
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
                id = default(int),
                name = "Lions enclosure",
                story = "Finalt they are here",
                language = nonExistantLang
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        public void UpdateEnclosureValidTest()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            monkeyEncEn.name = "kaki";
            enclosureController.UpdateEnclosure(monkeyEncEn);

            encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.AreEqual(3, encs.Count());

            var updatedEnc = encs.SingleOrDefault(en => en.name == "kaki");
            Assert.IsNotNull(updatedEnc);

            var nothing = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNull(nothing);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureExistingName()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                id = monkeyEncEn.id,
                language = monkeyEncEn.language,
                markerLatitude = monkeyEncEn.markerLatitude,
                markerLongitude = monkeyEncEn.markerLongitude,
                name = "Houman Monkeys"
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

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                id = monkeyEncEn.id,
                language = monkeyEncEn.language,
                markerLatitude = monkeyEncEn.markerLatitude,
                markerLongitude = monkeyEncEn.markerLongitude,
                name = ""
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

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                id = 1000000,
                language = monkeyEncEn.language,
                markerLatitude = monkeyEncEn.markerLatitude,
                markerLongitude = monkeyEncEn.markerLongitude,
                name = monkeyEncEn.name
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

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "Monkeys");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure human = new Enclosure
            {
                id = monkeyEncEn.id,
                language = nonExistantLang,
                markerLatitude = monkeyEncEn.markerLatitude,
                markerLongitude = monkeyEncEn.markerLongitude,
                name = monkeyEncEn.name
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

            var monkHeb = encsHeb.SingleOrDefault(en => en.name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            //delete animals
            AnimalController anCont = new AnimalController();
            var animalsList = anCont.GetAnimalsByEnclosure((int)monkHeb.id, (int)monkHeb.language);

            foreach (Animal an in animalsList)
            {
                anCont.DeleteAnimal((int)an.id);
            }

            //delete reccuringEvents
            var recEvents = enclosureController.GetRecurringEvents((int)monkHeb.id);

            foreach (RecurringEvent eve in recEvents)
            {
                enclosureController.DeleteRecurringEvent((int)eve.id);
            }

            enclosureController.DeleteEnclosure((int)monkHeb.id);
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

            var monkHeb = encsHeb.SingleOrDefault(en => en.name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            enclosureController.DeleteEnclosure((int)monkHeb.id);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteEnclosureRecurringEventsExists()
        {
            var encsHeb = enclosureController.GetAllEnclosures();
            Assert.IsNotNull(encsHeb);
            Assert.AreEqual(3, encsHeb.Count());

            var monkHeb = encsHeb.SingleOrDefault(en => en.name == "קופי אדם");
            Assert.IsNotNull(monkHeb);

            //delete animals
            AnimalController anCont = new AnimalController();
            var animalsList = anCont.GetAnimalsByEnclosure((int)monkHeb.id, (int)monkHeb.language);

            foreach (Animal an in animalsList)
            {
                anCont.DeleteAnimal((int)an.id);
            }

            enclosureController.DeleteEnclosure((int)monkHeb.id);
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
