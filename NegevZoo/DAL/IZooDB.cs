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

        public abstract DbSet<OpeningHour> GetAllOpeningHours();

        public abstract DbSet<ContactInfo> GetAllContactInfos();

        public abstract DbSet<SpecialEvent> GetAllSpecialEvents();

        public abstract DbSet<WallFeed> GetAllWallFeeds();

        public abstract DbSet<User> GetAllUsers();

        public abstract DbSet<Language> GetAllLanguages();

        public abstract DbSet<EnclosurePicture> GetAllEnclosurePictures();

        public abstract DbSet<YoutubeVideoUrl> GetAllEnclosureVideos();
        
        public abstract DbSet<Device> getAllDevices();
        #endregion

        #region Cache

        protected abstract List<TEntity> GetFromCache<TEntity>() where TEntity : class;

        protected abstract void SetInCache<TEntity>(List<TEntity> entity) where TEntity : class;







        #endregion
    }
}