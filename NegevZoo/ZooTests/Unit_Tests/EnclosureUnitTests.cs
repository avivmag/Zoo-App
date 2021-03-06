﻿using BL;
using DAL;
using DAL.Models;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;

namespace ZooTests.Unit_Tests
{
    [TestClass]
    public class EnclosureUnitTests
    {
        #region tests SetUp and TearDown

        private ZooContext context;
        private int nonExistantLang;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            context = new ZooContext(true);
            nonExistantLang = 100;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            DummyDB.CleanDb();
        }

        #endregion

        #region GetAllEnclosureResults()
        [TestMethod]
        public void GetAllEnclosuresResutlsLangHe()
        {
            Assert.AreEqual(4, context.GetAllEnclosureResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllEnclosuresResultsLangEng()
        {
            Assert.AreEqual(4, context.GetAllEnclosureResults((int)Languages.en).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAllEnclosuresResultsLangNotExist()
        {
            context.GetAllEnclosureResults(nonExistantLang);
        }
        #endregion
        
        #region GetAllEnclosures()
        [TestMethod]
        public void GetAllEnclosures()
        {
            IEnumerable<Enclosure> encs = context.GetAllEnclosures();

            Assert.AreEqual(4, encs.Count());
        }
        #endregion

        #region GetEnclosureDetailsById
        [TestMethod]
        public void GetEnclosureDetailsByIdValidInput()
        {
            // 2 details
            IEnumerable<EnclosureDetail> details = context.GetEnclosureDetailsById(1);

            Assert.AreEqual(2, details.Count());

            EnclosureDetail enc = details.First();
            Assert.AreEqual("תצוגת הקופים", enc.name);

            enc = details.Last();
            Assert.AreEqual("Monkeys", enc.name);

            //1 detial
            details = context.GetEnclosureDetailsById(4);
            Assert.AreEqual(1, details.Count());

            enc = details.First();
            Assert.AreEqual("קרנף לבן", enc.name);
        }

        [TestMethod]
        public void GetEnclosureDetailsByIdWrongId()
        {
            var details = context.GetEnclosureDetailsById(-4);
            Assert.AreEqual(0, details.Count());
        }
        #endregion

        #region GetAllRecurringEvents

        [TestMethod]
        public void GetAllRecutnigEventsValidInput()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = encs.ElementAt(0);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            var recurringEvents = context.GetRecurringEvents((int)enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(1, recurringEvents.Count());

            var recEvent = recurringEvents.ElementAt(0);
            Assert.IsNotNull(recEvent);
            Assert.AreEqual(1, recEvent.id);
            Assert.AreEqual(enc.Id, recEvent.enclosureId);
            Assert.AreEqual(enc.Language, recEvent.language);
            Assert.AreEqual(new TimeSpan(10, 30, 0), recEvent.startTime);
            Assert.AreEqual(new TimeSpan(11, 0, 0), recEvent.endTime);
        }

        [TestMethod]
        public void GetRecurringEventValidInputEmptyList()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = encs.SingleOrDefault(en => en.Name == "זברה");
            Assert.IsNotNull(enc);
            Assert.AreEqual(3, enc.Id);

            var recurringEvents = context.GetRecurringEvents((int)enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(0, recurringEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException),"Wrong input. enclosure id doesn't exists")]
        public void GetRecurringEventsWrongId()
        {
            var recEvents = context.GetRecurringEvents(200, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetRecurringEventsWrongLanguage()
        {
            context.GetRecurringEvents(1,nonExistantLang);
        }
        #endregion

        #region GetEnclosurePicturesById
        [TestMethod]
        public void GetEnclosurePictureByIdValidInput()
        {
            //only 1 result
            var pictures = context.GetEnclosurePicturesById(1);
            Assert.AreEqual(1, pictures.Count());
            Assert.AreEqual("url1", pictures.First().pictureUrl);

            //more than 1
            pictures = context.GetEnclosurePicturesById(2);
            Assert.AreEqual(2, pictures.Count());
            Assert.AreEqual("url2", pictures.First().pictureUrl);
            Assert.AreEqual("url3", pictures.Last().pictureUrl);
        }

        [TestMethod]
        public void GetEnclosurePictureByIdEmptyUrl()
        {
            var pictures = context.GetEnclosurePicturesById(4);
            Assert.AreEqual(0, pictures.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure doesn't exists")]
        public void GetEnclosurePictureByIdWrongId()
        {
            //only 1 result
            var pictures = context.GetEnclosurePicturesById(-4);
        }
        #endregion

        #region GetEnclosureVideosUrlById
        [TestMethod]
        public void GetEnclosureVideoUrlByIdValidInput()
        {
            //only 1 result
            var videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(1, videos.Count());
            Assert.AreEqual("video1", videos.First().videoUrl);

            //more than 1
            videos = context.GetEnclosureVideosById(2);
            Assert.AreEqual(2, videos.Count());
            Assert.AreEqual("video2", videos.First().videoUrl);
            Assert.AreEqual("video3", videos.Last().videoUrl);
        }

        [TestMethod]
        public void GetEnclosureVideoByIdEmptyUrl()
        {
            //only 1 result
            var videos = context.GetEnclosureVideosById(4);
            Assert.AreEqual(0, videos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The enclosure doesn't exists")]
        public void GetEnclosureVideoByIdWrongId()
        {
            context.GetEnclosureVideosById(-4);
        }
        #endregion

        #region UpdateEnclosure
        [TestMethod]
        public void UpdateEnclosureAddEncValidTest()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.AreEqual(4, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = "Lions enclosure"
            };

            context.UpdateEnclosure(enc);

            encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.AreEqual(4, encs.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding enclosure. Name already exists")]
        public void UpdateEnclosureAddExistingName()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = "תצוגת הקופים",
            };

            context.UpdateEnclosure(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. enclosure name is null or white space")]
        public void UpdateEnclosureAddEmptyName()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = ""
            };

            context.UpdateEnclosure(enc);
        }

        [TestMethod]
        public void UpdateEnclosureValidInput()
        {
            var encs = context.GetAllEnclosures();

            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            monkeyEncEn.name = "קקי";
            context.UpdateEnclosure(monkeyEncEn);

            encs = context.GetAllEnclosures();
            Assert.AreEqual(4, encs.Count());

            var updatedEnc = encs.SingleOrDefault(en => en.name == "קקי");
            Assert.IsNotNull(updatedEnc);

            var nothing = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNull(nothing);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating enclosure. Name already exsits")]
        public void UpdateEnclosureExistingName()
        {
            var encs = context.GetAllEnclosures();
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure zebra = new Enclosure
            {
                id = monkeyEncEn.id,
                markerY = monkeyEncEn.markerY,
                markerX = monkeyEncEn.markerX,
                name = "זברה"
            };

            context.UpdateEnclosure(zebra);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. enclosure name is null or white space")]
        public void UpdateEnclosureEmptyName()
        {
            var encs = context.GetAllEnclosures();
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure zebra = new Enclosure
            {
                id = monkeyEncEn.id,
                markerY = monkeyEncEn.markerY,
                markerX = monkeyEncEn.markerX,
                name = ""
            };

            context.UpdateEnclosure(zebra);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Enclosure id doesn't exits")]
        public void UpdateEnclosureDoesntExists()
        {
            var encs = context.GetAllEnclosures();
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure zebra = new Enclosure
            {
                id = -4,
                markerY = monkeyEncEn.markerY,
                markerX = monkeyEncEn.markerX,
                name = "זברה"
            };

            context.UpdateEnclosure(zebra);
        }
        #endregion

        #region UpdateEnclosureDetails

        [TestMethod]
        public void UpdateEnclosureDetailsAddEncDetValidInput()
        {
            var details = context.GetEnclosureDetailsById(4);
            Assert.AreEqual(1, details.Count());
            Assert.AreEqual("קרנף לבן", details.First().name);

            var enc = new EnclosureDetail
            {
                encId = 4,
                name = "Rhino enclosure",
                language = (int)Languages.en,
                story = "This is a story"
            };

            context.UpdateEnclosureDetails(enc);

            details = context.GetEnclosureDetailsById(4);
            Assert.AreEqual(2, details.Count());
            Assert.AreEqual("Rhino enclosure", details.SingleOrDefault(d => d.language == (int)Languages.en).name);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The name of the enclosure alreay exists.")]
        public void UpdateEnclosureDetailsAddEncDetExistingName()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new EnclosureDetail
            {
                encId = 4,
                name = "Monkeys",
                story = "Finalt they are here",
                language = (int)Languages.en
            };

            context.UpdateEnclosureDetails(enc);
        }

        [TestMethod]
        public void UpdateEnclosureDetailsUpdateEncDetValidInput()
        {
            var details = context.GetEnclosureDetailsById(4);
            Assert.AreEqual(1, details.Count());
            Assert.AreEqual("קרנף לבן", details.First().name);

            var enc = new EnclosureDetail
            {
                encId = 2,
                language = (int)Languages.he, //hebrew
                name = "קופי אדם",
                story = "הקופים שלנו הם הכי חכמים!"

            };

            enc.name = "123";

            context.UpdateEnclosureDetails(enc);

            details = context.GetEnclosureDetailsById(2);
            Assert.AreEqual(2, details.Count());
            Assert.AreEqual("123", details.SingleOrDefault(d => d.language == (int)Languages.he).name);
            Assert.AreEqual("Houman Monkeys", details.SingleOrDefault(d => d.language == (int)Languages.en).name);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Enclosure detail id doesn't exists.")]
        public void UpdateEnclosureDetailsEncDoesntExists()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.AreEqual(4, encs.Count());

            var enc = new EnclosureDetail
            {
                encId = -4,
                name = "Lions enclosure",
                language = (int)Languages.en,
                story = "This is a story"
            };

            context.UpdateEnclosureDetails(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. enclosure detail name is empty or white space")]
        public void UpdateEnclosureDetailsEmptyName()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new EnclosureDetail
            {
                encId = 2,
                name = "",
                story = "Finalt they are here",
                language = (int)Languages.en
            };

            context.UpdateEnclosureDetails(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void UpdateEnclosureDetailsWrongLanguage()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new EnclosureDetail
            {
                encId = 2,
                name = "Monkeys",
                story = "Finalt they are here",
                language = -4
            };

            context.UpdateEnclosureDetails(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input in updating enclosure. Name already exsits")]
        public void UpdateEnclosureDetailsUpdateEncDetExistingName()
        {
            var encs = context.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(4, encs.Count());

            var enc = new EnclosureDetail
            {
                encId = 2,
                language = (int)Languages.en, //english
                name = "Houman Monkeys",
                story = "Computer Science students."

            };

            enc.name = "Monkeys";

            context.UpdateEnclosureDetails(enc);
        }

        #endregion

        #region UpdateVideoUrl

        [TestMethod]
        public void UpdateEnclosureVideoAddEncVidValidInput()
        {
            var videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(1, videos.Count());

            var vid = videos.First();
            Assert.AreEqual("video1", vid.videoUrl);
            Assert.AreEqual(1, vid.enclosureId);

            var newVid = new YoutubeVideoUrl
            {
                id = default(int),
                enclosureId = 1,
                videoUrl = "video4"
            };

            context.UpdateEnclosureVideo(newVid);

            videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(2, videos.Count());
            Assert.AreEqual("video4", videos.SingleOrDefault(p => p.videoUrl == "video4").videoUrl);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Enclosure doesn't exists")]
        public void UpdateEnclosureVidoeEncDoesntExists()
        {
            var videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(1, videos.Count());

            var vid = videos.First();
            Assert.AreEqual("video1", vid.videoUrl);
            Assert.AreEqual(1, vid.enclosureId);

            var newVid = new YoutubeVideoUrl
            {
                id = default(int),
                enclosureId = -1,
                videoUrl = "video4"
            };

            context.UpdateEnclosureVideo(newVid);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The url is empty or white spaces")]
        public void UpdateEnclosureVideosEmptyUrl()
        {
            var videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(1, videos.Count());

            var vid = videos.First();
            Assert.AreEqual("video1", vid.videoUrl);
            Assert.AreEqual(1, vid.enclosureId);

            var newVid = new YoutubeVideoUrl
            {
                id = default(int),
                enclosureId = 1,
                videoUrl = ""
            };

            context.UpdateEnclosureVideo(newVid);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding enclosure video. The enclosure vido url already exists")]
        public void UpdateEnclosureVideoAddEncVidUrlExists()
        {
            var videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(1, videos.Count());

            var vid = videos.First();
            Assert.AreEqual("video1", vid.videoUrl);
            Assert.AreEqual(1, vid.enclosureId);

            var newVid = new YoutubeVideoUrl
            {
                id = default(int),
                enclosureId = 1,
                videoUrl = "video2"
            };

            context.UpdateEnclosureVideo(newVid);
        }

        #endregion

        #region UpdateRecurringEvent

        [TestMethod]
        public void UpdateRecurringEventAddRecEventValidInput()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 1,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);

            events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(3, events.Count());
            Assert.IsNotNull(events.SingleOrDefault(re => re.description == "Looking"));
        }

        [TestMethod]
        public void UpdateRecurringEventAddRecEventCloseTimeValidInput()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 7,
                description = "Looking",
                startTime = new TimeSpan(10, 00, 00),
                endTime = new TimeSpan(10, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);

            events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(3, events.Count());
            Assert.IsNotNull(events.SingleOrDefault(re => re.description == "Looking"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The descrioption is null or white spaces")]
        public void UpdateRecurringEventEmptyDesc()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 12,
                description = "",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);

            events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(3, events.Count());
            Assert.IsNotNull(events.SingleOrDefault(re => re.description == "Looking"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Enclosure doesn't exists")]
        public void UpdateRecurringEventWrongEnclosure()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = -2,
                day = 12,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The day is not defined")]
        public void UpdateRecurringEventWrongDay()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 8,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        public void UpdateRecurringEventWrongDayAccordingToLanguage()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 2,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = (int)Languages.en
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The start time is bigger or equal to the end time")]
        public void UpdateRecurringEventWrongTime()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 12,
                description = "Looking",
                endTime = new TimeSpan(10, 30, 00),
                startTime = new TimeSpan(11, 30, 00),
                language = 2
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The start time is bigger or equal to the end time")]
        public void UpdateRecurringEventWrongTimeEndEqualsStart()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 12,
                description = "Looking",
                endTime = new TimeSpan(11, 30, 00),
                startTime = new TimeSpan(11, 30, 00),
                language = 2
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void UpdateRecurringEventWrongLanguage()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 12,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = -1
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding recurring event. There is another recurring event in the same time")]
        public void UpdateRecurringEventAddEventTimeExists()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = default(int),
                enclosureId = 2,
                day = 17,
                description = "Looking",
                startTime = new TimeSpan(10, 30, 00),
                endTime = new TimeSpan(11, 30, 00),
                language = 2
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The day is not defined")]
        public void UpdateRecurringEventUpdateRecEventValidInput()
        {
            var events = context.GetRecurringEvents(1, (int)Languages.he);
            Assert.AreEqual(1, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(1, recEvent.id);
            Assert.AreEqual(1, recEvent.day);
            Assert.AreEqual("האכלה", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = 6,
                enclosureId = 2,
                day = 17,
                description = "Feeding",
                startTime = new TimeSpan(10, 30, 0),
                endTime = new TimeSpan(11, 0, 0),
                language = (int)Languages.en
            };

            newRecEvent.description = "kaki";
            newRecEvent.day = 16;

            context.UpdateRecurringEvent(newRecEvent);

            events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());
            Assert.IsNotNull(events.SingleOrDefault(re => re.description == "kaki"));
            Assert.IsNotNull(events.SingleOrDefault(re => re.day == 16));
            Assert.IsNull(events.SingleOrDefault(re => re.day == 17));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. RecurringEvent doesn't exists")]
        public void UpdateRecurringEventUpdateEventDoesntExists()
        {
            var events = context.GetRecurringEvents(2, (int)Languages.en);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = -1,
                enclosureId = 1,
                day = 1,
                description = "האכלה",
                startTime = new TimeSpan(10, 30, 0),
                endTime = new TimeSpan(11, 0, 0),
                language = (int)Languages.he
            };

            context.UpdateRecurringEvent(newRecEvent);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating enclosure video. The enclosure vido url already exists")]
        public void UpdateRecurringEventUpdateTimeExists()
        {
            var events = context.GetRecurringEvents(2, 2);
            Assert.AreEqual(2, events.Count());

            var recEvent = events.First();
            Assert.AreEqual(4, recEvent.id);
            Assert.AreEqual(11, recEvent.day);
            Assert.AreEqual("Playing", recEvent.description);

            var newRecEvent = new RecurringEvent
            {
                id = 6,
                enclosureId = 2,
                day = 17,
                description = "Feeding",
                startTime = new TimeSpan(10, 30, 0),
                endTime = new TimeSpan(11, 0, 0),
                language = (int)Languages.en
            };

            newRecEvent.day = 11;
            newRecEvent.startTime = new TimeSpan(13, 15, 0);
            newRecEvent.endTime = new TimeSpan(13, 45, 0);

            context.UpdateRecurringEvent(newRecEvent);
        }

        #endregion

        #region DeleteEnclosure

        [TestMethod]
        public void DeleteEnclosureValidInput()
        {
            var encs = context.GetAllEnclosures();
            var details = context.GetEnclosureDetailsById(4);

            Assert.AreEqual(0, context.GetRecurringEvents(4, (int)Languages.he).Count());
            Assert.AreEqual(0, context.GetAnimalResultByEnclosure(4, (int)Languages.he).Count());
            Assert.AreEqual(4, encs.Count());
            Assert.AreEqual(1, details.Count());

            context.DeleteEnclosure(4);

            encs = context.GetAllEnclosures();
            Assert.AreEqual(3, encs.Count());

            details = context.GetEnclosureDetailsById(4);
            Assert.AreEqual(0, details.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidOperationException), "Threre are animals that related to this enclosure")]
        public void DeleteEnclosureExistingAnimals()
        {
            var encs = context.GetAllEnclosures();
            var details = context.GetEnclosureDetailsById(1);

            var recurringEvents = context.GetRecurringEvents(1, 1);
            Assert.AreEqual(1, recurringEvents.Count());
            context.DeleteRecurringEvent(1, (int)recurringEvents.First().id);
            Assert.AreEqual(0, context.GetRecurringEvents(1, 1).Count());

            Assert.AreEqual(2, context.GetAnimalResultByEnclosure(1, (int)Languages.he).Count());
            Assert.AreEqual(4, encs.Count());
            Assert.AreEqual(2, details.Count());

            context.DeleteEnclosure(1);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidOperationException), "Threre are recurring events that related to this enclosure")]
        public void DeleteEnclosureExistingRecurringEvents()
        {
            var encs = context.GetAllEnclosures();
            var details = context.GetEnclosureDetailsById(1);

            var animals = context.GetAnimalResultByEnclosure(1, (int)Languages.he).ToList();
            Assert.AreEqual(2, animals.Count());
            foreach (AnimalResult a in animals)
            {
                context.DeleteAnimal((int)a.Id);
            }
            animals = context.GetAnimalResultByEnclosure(1, (int)Languages.he).ToList();
            Assert.AreEqual(0, animals.Count());

            Assert.AreEqual(4, encs.Count());
            Assert.AreEqual(2, details.Count());

            Assert.AreEqual(1, context.GetRecurringEvents(1, 1).Count());

            context.DeleteEnclosure(1);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. enclosure ID doesn't exists.")]
        public void DeleteEnclosureWrongId()
        {
            context.DeleteEnclosure(-1);
        }

        #endregion

        #region DeleteEnclosurePicture

        [TestMethod]
        public void DeleteEnclosurePictureValidInput()
        {
            var pictures = context.GetEnclosurePicturesById(2);

            Assert.AreEqual(2, pictures.Count());

            var pic = pictures.First();
            Assert.AreEqual("url2", pic.pictureUrl);

            context.DeleteEnclosurePicture(pic.enclosureId, (int)pic.id);

            pictures = context.GetEnclosurePicturesById(2);
            Assert.AreEqual(1, pictures.Count());

            pic = pictures.First();
            Assert.AreEqual("url3", pic.pictureUrl);

            context.DeleteEnclosurePicture(pic.enclosureId, (int)pic.id);

            pictures = context.GetEnclosurePicturesById(2);
            Assert.AreEqual(0, pictures.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Cannot delete a picture of another enclosure.")]
        public void DeleteEnclosurePictureWrongPicId()
        {
            context.DeleteEnclosurePicture(1, -1);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidOperationException), "Wrong input. The enclsure picture doesn't exists")]
        public void DeleteEnclosurePictureWrongEncId()
        {
            context.DeleteEnclosurePicture(-1, 1);
        }
        #endregion

        #region DeleteEnclosureVideo

        [TestMethod]
        public void DeleteEnclosureVideoValidInput()
        {
            var videos = context.GetEnclosureVideosById(1);

            Assert.AreEqual(1, videos.Count());

            var vid = videos.First();
            Assert.AreEqual("video1", vid.videoUrl);

            context.DeleteEnclosureVideo((int)vid.enclosureId, (int)vid.id);

            videos = context.GetEnclosureVideosById(1);
            Assert.AreEqual(0, videos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The video doesn't exist or does not belong to the enclosure")]
        public void DeleteEnclosureVideoWrongEncId()
        {
            context.DeleteEnclosureVideo(-1, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The video doesn't exist or does not belong to the enclosure")]
        public void DeleteEnclosureVideoWrongId()
        {
            context.DeleteEnclosureVideo(1, -1);
        }

        #endregion

        #region DeleteRecurringEvent
        [TestMethod]
        public void DeleteRecurringEventValidInput()
        {
            var enc = context.GetAllEnclosures().SingleOrDefault(e => e.name == "קופי אדם");
            Assert.IsNotNull(enc);

            var allRecEve = context.GetRecurringEvents((int)enc.id, (int)Languages.he);
            Assert.AreEqual(2, allRecEve.Count());

            var eve = allRecEve.SingleOrDefault(e => e.id == 3);
            Assert.AreEqual(eve.day, 1);
            Assert.AreEqual(eve.description, "משחק");

            context.DeleteRecurringEvent((int)enc.id, (int)eve.id);

            allRecEve = context.GetRecurringEvents((int)enc.id, (int)Languages.he);
            Assert.AreEqual(1, allRecEve.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. RecurringEvent's id doesn't exists.")]
        public void DeleteRecuringEventIdDoesntExists()
        {
            context.DeleteRecurringEvent(1, 1000);
        }
        #endregion
    }
}
