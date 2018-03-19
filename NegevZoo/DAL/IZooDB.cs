﻿using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend.Models;

namespace DAL
{
    public abstract class IZooDB : DbContext
    {
        #region DbSets

        protected DbSet<Animal> Animals { get; set; }
        protected DbSet<Enclosure> Enclosures { get; set; }
        protected DbSet<RecurringEvent> RecurringEvents { get; set; }
        protected DbSet<WallFeed> WallFeeds { get; set; }
        protected DbSet<Price> Prices { get; set; }
        protected DbSet<GeneralInfo> GeneralInfo { get; set; }
        protected DbSet<OpeningHour> OpeningHours { get; set; }
        protected DbSet<ContactInfo> ContactInfos { get; set; }
        protected DbSet<SpecialEvent> SpecialEvents { get; set; }
        protected DbSet<WorkerUser> WorkerUsers { get; set; }
        protected DbSet<Language> AllLanguages { get; set; }

        #endregion 

        #region Getters

        public DbSet<Animal> GetAllAnimals()
        {
            return Animals;
        }

        public DbSet<Enclosure> GetAllEnclosures()
        {
            return Enclosures;
        }


        public DbSet<RecurringEvent> GetAllRecuringEvents()
        {
            return RecurringEvents;
        }

        public DbSet<Price> GetAllPrices()
        {
            return Prices;
        }

        public DbSet<GeneralInfo> GetGeneralInfo()
        {
            return GeneralInfo;
        }

        public DbSet<OpeningHour> GetAllOpeningHours()
        {
            return OpeningHours;
        }

        public DbSet<ContactInfo> GetAllContactInfos()
        {
            return ContactInfos;
        }

        public DbSet<SpecialEvent> GetAllSpecialEvents()
        {
            return SpecialEvents;
        }

        public DbSet<WallFeed> GetAllWallFeeds()
        {
            return WallFeeds;
        }

        public DbSet<WorkerUser> GetAllUsers()
        {
            return WorkerUsers;
        }

        public DbSet<Language> getAllLanguages()
        {
            return AllLanguages;
        }

        #endregion

        #region Cache

        protected abstract List<TEntity> GetFromCache<TEntity>() where TEntity : class;

        protected abstract void SetInCache<TEntity>(List<TEntity> entity) where TEntity : class;





        #endregion
    }
}