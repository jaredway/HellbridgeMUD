package com.planet_ink.coffee_mud.Races;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class SchoolMonster extends StdRace
{
	public SchoolMonster()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="School Monster";
		// inches
		shortestMale=24;
		shortestFemale=24;
		heightVariance=12;
		// pounds
		lightestWeight=60;
		weightVariance=60;
	}
	public boolean playerSelectable(){return false;}

	public void affectCharStats(MOB affectedMOB, CharStats affectableStats)
	{
		super.affectCharStats(affectedMOB, affectableStats);
		affectableStats.setStat(CharStats.STRENGTH,1);
		affectableStats.setStat(CharStats.DEXTERITY,1);
		affectableStats.setStat(CharStats.INTELLIGENCE,3);
	}
	public Weapon myNaturalWeapon()
	{
		if(naturalWeapon==null)
		{
			naturalWeapon=CMClass.getWeapon("StdWeapon");
			naturalWeapon.setName("sharp claws");
			naturalWeapon.setWeaponType(Weapon.TYPE_SLASHING);
		}
		return naturalWeapon;
	}
	public String healthText(MOB mob)
	{
		double pct=(Util.div(mob.curState().getHitPoints(),mob.maxState().getHitPoints()));

		if(pct<.10)
			return "^r" + mob.name() + "^r is hovering on deaths door!^N";
		else
		if(pct<.20)
			return "^r" + mob.name() + "^r is covered in torn slabs of flesh.^N";
		else
		if(pct<.30)
			return "^r" + mob.name() + "^r is gored badly with lots of tears.^N";
		else
		if(pct<.40)
			return "^y" + mob.name() + "^y has numerous gory tears and gashes.^N";
		else
		if(pct<.50)
			return "^y" + mob.name() + "^y has some gory tears and gashes.^N";
		else
		if(pct<.60)
			return "^p" + mob.name() + "^p has a few gory wounds.^N";
		else
		if(pct<.70)
			return "^p" + mob.name() + "^p is cut and bruised heavily.^N";
		else
		if(pct<.80)
			return "^g" + mob.name() + "^g has some minor cuts and bruises.^N";
		else
		if(pct<.90)
			return "^g" + mob.name() + "^g has a few bruises and scratches.^N";
		else
		if(pct<.99)
			return "^g" + mob.name() + "^g has a few small bruises.^N";
		else
			return "^c" + mob.name() + "^c is in perfect health^N";
	}
}
