package com.planet_ink.coffee_mud.Races;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class WereWolf extends GiantWolf
{
	public String ID(){	return "WereWolf"; }
	public String name(){ return "WereWolf"; }
	protected int shortestMale(){return 59;}
	protected int shortestFemale(){return 59;}
	protected int heightVariance(){return 12;}
	protected int lightestWeight(){return 80;}
	protected int weightVariance(){return 80;}
	protected long forbiddenWornBits(){return 0;}
	
	protected static Vector resources=new Vector();
	public void affectCharStats(MOB affectedMOB, CharStats affectableStats)
	{
		affectableStats.setStat(CharStats.DEXTERITY,affectableStats.getStat(CharStats.DEXTERITY)+3);
	}

	public Vector myResources()
	{
		synchronized(resources)
		{
			if(resources.size()==0)
			{
				resources.addElement(makeResource
				("some "+name().toLowerCase()+" claws",EnvResource.RESOURCE_BONE));
				for(int i=0;i<4;i++)
					resources.addElement(makeResource
					("a strip of "+name().toLowerCase()+" hide",EnvResource.RESOURCE_FUR));
				for(int i=0;i<2;i++)
					resources.addElement(makeResource
					("a pound of "+name().toLowerCase()+" meat",EnvResource.RESOURCE_MEAT));
				resources.addElement(makeResource
				("some "+name().toLowerCase()+" blood",EnvResource.RESOURCE_BLOOD));
				resources.addElement(makeResource
				("a pile of "+name().toLowerCase()+" bones",EnvResource.RESOURCE_BONE));
			}
		}
		return resources;
	}
}