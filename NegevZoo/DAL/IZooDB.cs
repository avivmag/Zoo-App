using DAL.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL
{
    public abstract class IZooDB : DbContext
    {
        public IZooDB(string name) : base(name) { }

        public IZooDB() { }

        #region Getters

        public abstract DbSet<Animal> GetAllAnimals();

        public abstract DbSet<AnimalDetail> GetAllAnimalsDetails();

        public abstract DbSet<Enclosure> GetAllEnclosures();

        public abstract DbSet<EnclosureDetail> GetAllEnclosureDetails();

        public abstract DbSet<RecurringEvent> GetAllRecuringEvents();

        public abstract DbSet<Price> GetAllPrices();

        public abstract DbSet<GeneralInfo> GetGeneralInfo();

        public abstract DbSet<MiscMarker> GetAllMiscMarkers();

        public abstract DbSet<OpeningHour> GetAllOpeningHours();

        public abstract DbSet<ContactInfo> GetAllContactInfos();

        public abstract DbSet<SpecialEvent> GetAllSpecialEvents();

        public abstract DbSet<WallFeed> GetAllWallFeeds();

        public abstract DbSet<User> GetAllUsers();

        public abstract DbSet<Language> GetAllLanguages();

        public abstract DbSet<EnclosurePicture> GetAllEnclosurePictures();

        public abstract DbSet<YoutubeVideoUrl> GetAllEnclosureVideos();
        
        public abstract DbSet<Device> GetAllDevices();

        public abstract DbSet<MapInfo> GetAllMapInfos();

        public abstract DbSet<Route> GetAllRoutes();

        public abstract DbSet<UserSession> GetAllUserSessions();

        public abstract DbSet<AnimalStory> GetAllAnimalStories();

        public abstract DbSet<AnimalStoryDetail> GetAllAnimalStoryDetails();

        #endregion

        #region Cache

        public abstract List<T> GetFromCache<T>(DbSet<T> table) where T : class;

        public abstract void RemoveFromCache(string key);
        #endregion
    }
}