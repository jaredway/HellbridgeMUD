package com.planet_ink.coffee_mud.Abilities.Prayers;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

/* 
   Copyright 2000-2005 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Prayer_ProtUndead extends Prayer
{
	public String ID() { return "Prayer_ProtUndead"; }
	public String name(){ return "Protection Undead";}
	public String displayText(){ return "(Protection/Undead)";}
	public int quality(){ return OK_SELF;}
	public long flags(){return Ability.FLAG_HOLY|Ability.FLAG_UNHOLY;}
	protected int canAffectCode(){return Ability.CAN_MOBS;}
	protected int canTargetCode(){return Ability.CAN_MOBS;}

	public boolean tick(Tickable ticking, int tickID)
	{
		if((affected==null)||(!(affected instanceof MOB)))
			return false;

		if(invoker==null)
			return false;

		MOB mob=(MOB)affected;

		if(mob.charStats().getMyRace().racialCategory().equalsIgnoreCase("Undead"))
		{
			int damage=(int)Math.round(Util.div(mob.envStats().level(),3.0));
			MUDFight.postDamage(invoker,mob,this,damage,CMMsg.MASK_GENERAL|CMMsg.TYP_CAST_SPELL,Weapon.TYPE_BURSTING,"^S<T-HIS-HER> protective aura <DAMAGE> <T-NAME>!^?");
		}
		return super.tick(ticking,tickID);
	}

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if(affected==null) return;
		if(!(affected instanceof MOB)) return;
		MOB mob=(MOB)affected;

		if(mob.isInCombat())
		{
			MOB victim=mob.getVictim();
			if(victim.charStats().getMyRace().racialCategory().equalsIgnoreCase("Undead"))
				affectableStats.setArmor(affectableStats.armor()-50);
		}
	}



	public void unInvoke()
	{
		// undo the affects of this spell
		if((affected==null)||(!(affected instanceof MOB)))
			return;
		MOB mob=(MOB)affected;

		super.unInvoke();

		if(canBeUninvoked())
			mob.tell("Your protection from undead fades.");
	}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel)
	{
        Environmental target=mob;
        if((auto)&&(givenTarget!=null)) target=givenTarget;
        if(target.fetchEffect(this.ID())!=null)
		{
			mob.tell("You are already affected by "+name()+".");
			return false;
		}

		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;

		boolean success=profficiencyCheck(mob,0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			FullMsg msg=new FullMsg(mob,target,this,affectType(auto),auto?"<T-NAME> become(s) protected from the undead.":"^S<S-NAME> "+prayWord(mob)+" for protection from the undead.^?");
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				beneficialAffect(mob,target,asLevel,0);
			}
		}
		else
			return beneficialWordsFizzle(mob,null,"<S-NAME> "+prayWord(mob)+" for protection, but there is no answer.");


		// return whether it worked
		return success;
	}
}
