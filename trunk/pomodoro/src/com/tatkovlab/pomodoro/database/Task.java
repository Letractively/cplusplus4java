package com.tatkovlab.pomodoro.database;

public class Task
{
  private long abandoned;
  private long dateAdded;
  private long done;
  private long estimated;
  private long id;
  private TASK_LIST list;
  private String name;
  private TASK_STATUS status;

  public long getAbandoned()
  {
    return this.abandoned;
  }

  public long getDateAdded()
  {
    return this.dateAdded;
  }

  public long getDone()
  {
    return this.done;
  }

  public long getEstimated()
  {
    return this.estimated;
  }

  public long getId()
  {
    return this.id;
  }

  public TASK_LIST getList()
  {
    return this.list;
  }

  public String getName()
  {
    return this.name;
  }

  public TASK_STATUS getStatus()
  {
    return this.status;
  }

  public void setAbandoned(long paramLong)
  {
    this.abandoned = paramLong;
  }

  public void setDateAdded(long paramLong)
  {
    this.dateAdded = paramLong;
  }

  public void setDone(long paramLong)
  {
    this.done = paramLong;
  }

  public void setEstimated(long paramLong)
  {
    this.estimated = paramLong;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public void setList(TASK_LIST paramTASK_LIST)
  {
    this.list = paramTASK_LIST;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setStatus(TASK_STATUS paramTASK_STATUS)
  {
    this.status = paramTASK_STATUS;
  }

  public String toString()
  {
    return this.name + " " + this.estimated;
  }

  public static enum TASK_LIST
  {
	  ARCHIVE(2L),TODAY(1L);
	  
	  
    final long numericValue;

    static
    {
      TASK_LIST[] arrayOfTASK_LIST = new TASK_LIST[2];
      arrayOfTASK_LIST[0] = TODAY;
      arrayOfTASK_LIST[1] = ARCHIVE;
    }

    private TASK_LIST(long arg3)
    {
      this.numericValue = arg3;
    }

    public static TASK_LIST numValueToEnum(long paramLong)
    {
       for(TASK_LIST one :TASK_LIST.values() ){
    	   if( one.getNumericValue()==paramLong ){
    		   return one;
    	   }
       }
    	
    	return null;
    }

    public long getNumericValue()
    {
      return this.numericValue;
    }
  }

  public static enum TASK_STATUS
  {
	  NEW(1L),IN_PROGRESS(2L),FINISHED(3L);
	  
    final long numericValue;
    
    static
    {
      TASK_STATUS[] arrayOfTASK_STATUS = new TASK_STATUS[3];
      arrayOfTASK_STATUS[0] = NEW;
      arrayOfTASK_STATUS[1] = IN_PROGRESS;
      arrayOfTASK_STATUS[2] = FINISHED;
    }

    private TASK_STATUS(long arg3)
    {
      this.numericValue = arg3;
    }

    public static TASK_STATUS numValueToEnum(long paramLong)
    {
    	for( TASK_STATUS one:TASK_STATUS.values() ){
    		if( one.getNumericValue()==paramLong ){
    			return one;
    		}
    	}
    	
    	return null;
    }

    public long getNumericValue()
    {
      return this.numericValue;
    }
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.database.Task
 * JD-Core Version:    0.6.2
 */