using System;
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
        protected DbSet<WallFeed> WallFeeds { get; set; }

        #endregion 

        #region Getters

        public DbSet<Animal> GetAnimals()
        {
            return Animals;
        }

        public DbSet<Enclosure> GetEnclosures()
        {
            return Enclosures;
        }

        public IEnumerable<WallFeed> GetWallFeed()
        {
            return WallFeeds;
        }

        #endregion

        #region Cache

        protected abstract List<TEntity> GetFromCache<TEntity>() where TEntity : class;

        protected abstract void SetInCache<TEntity>(List<TEntity> entity) where TEntity : class;

     

        #endregion
    }
}