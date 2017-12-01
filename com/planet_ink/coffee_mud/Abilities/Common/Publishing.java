package com.planet_ink.coffee_mud.Abilities.Common;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.MiniJSON.MJSONException;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/*
   Copyright 2017-2017 Bo Zimmerman

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
public class Publishing extends CommonSkill
{
	@Override
	public String ID()
	{
		return "Publishing";
	}

	private final static String	localizedName	= CMLib.lang().L("Publishing");

	@Override
	public String name()
	{
		return localizedName;
	}

	private static final String[]	triggerStrings	= I(new String[] { "PUBLISH", "PUBLISHING" });

	@Override
	public String[] triggerStrings()
	{
		return triggerStrings;
	}

	@Override
	public int classificationCode()
	{
		return Ability.ACODE_COMMON_SKILL | Ability.DOMAIN_LEGAL;
	}

	protected double	price	= 1000.0;
	protected Item		found	= null;
	protected boolean	success = false;

	@Override
	protected boolean canBeDoneSittingDown()
	{
		return true;
	}

	public Publishing()
	{
		super();
		displayText=L("You are publishing...");
		verb=L("publishing");
	}

	public MiniJSON.JSONObject getData()
	{
		if(text().length()==0)
			super.setMiscText("{\"lastpub\":0}");
		try
		{
			return new MiniJSON().parseObject(text());
		}
		catch (MJSONException e)
		{
			Log.errOut(e);
			return new MiniJSON.JSONObject();
		}
	}
	
	public void setData(MiniJSON.JSONObject obj)
	{
		super.setMiscText(obj.toString());
	}
	
	@Override
	public void unInvoke()
	{
		if(canBeUninvoked())
		{
			if((affected!=null)
			&&(affected instanceof MOB)
			&&(!aborted)
			&&(!helping))
			{
				final MOB mob=(MOB)affected;
				if((!success)||(found==null))
					commonTell(mob,L("You messed up your attempt to get published."));
				else
				{
					final ArrayList<Room> rooms=new ArrayList<Room>();
					final ArrayList<ShopKeeper> shops=new ArrayList<ShopKeeper>();
					final TrackingLibrary.TrackingFlags flags;
					flags = CMLib.tracking().newFlags()
							.plus(TrackingLibrary.TrackingFlag.NOEMPTYGRIDS)
							.plus(TrackingLibrary.TrackingFlag.NOHIDDENAREAS)
							.plus(TrackingLibrary.TrackingFlag.NOHOMES);
					final int radius=10 + (10 * super.getXLEVELLevel(mob));
					CMLib.tracking().getRadiantRooms(mob.location(),rooms,flags,null,radius,null);
					for(int r=0;r<rooms.size();r++)
					{
						final Room R=rooms.get(r);
						final ShopKeeper rSK=CMLib.coffeeShops().getShopKeeper(R);
						if((rSK != null)
						&&(rSK.isSold(ShopKeeper.DEAL_BOOKS)))
							shops.add(rSK);
						for(int i=0;i<R.numInhabitants();i++)
						{
							final MOB M=R.fetchInhabitant(i);
							final ShopKeeper SK=CMLib.coffeeShops().getShopKeeper(M);
							if((SK != null)
							&&(SK.isSold(ShopKeeper.DEAL_BOOKS)))
								shops.add(SK);
						}
					}
					rooms.clear();
					if(shops.size()==0)
						commonTell(mob,L("There were no appropriate book shops nearby to publish at."));
					else
					{
						int pubbed = 0;
						int already = 0;
						int illegal = 0;
						int failed = 0;
						if(found.fetchEffect("Copyright")==null)
						{
							final Ability copyA=CMClass.getAbility("Copyright");
							if(copyA!=null)
							{
								copyA.setMiscText(mob.Name());
								found.addNonUninvokableEffect(copyA);
							}
						}
						Item shopItem = (Item)found.copyOf();
						if(shopItem.fetchEffect(ID())==null)
						{
							Publishing pubA=(Publishing)this.copyOf();
							shopItem.addNonUninvokableEffect(pubA);
						}
						for(final ShopKeeper SK : shops)
						{
							final Iterator<Environmental> ie=SK.getShop().getStoreInventory("$"+shopItem.Name()+"$");
							boolean proceed = true;
							if(ie.hasNext())
							{
								proceed = false;
								Environmental E=ie.next();
								if(E instanceof Item)
								{
									Ability copyrightA=((Item) E).fetchEffect("Copyright");
									if(copyrightA != null)
									{
										if(!copyrightA.text().equals(mob.Name()))
											illegal++;
										else
											already++;
									}
									else
										failed++;
								}
								else
									failed++;
							}
							if(proceed)
							{
								Log.infoOut("The book "+shopItem.Name()+" was published by "+mob.Name()+" to "+CMLib.map().roomLocation(SK));
								pubbed++;
								SK.getShop().addStoreInventory((Item)shopItem.copyOf(), Integer.MAX_VALUE/2, (int)CMath.round(price));
							}
						}
						StringBuilder str=new StringBuilder(L("Publishing completed. "));
						if(pubbed == 0)
							str.append(L("No copies were placed on local bookshelves.  Perhaps try another city?  "));
						else
						{
							str.append(L("@x1 cop(ys) were placed on local bookshelves.  ",""+pubbed));
							final Room R=mob.location();
							if(R!=null)
							{
								final Area A=R.getArea();
								if(A!=null)
								{
									final TimeClock C=A.getTimeObj();
									final MiniJSON.JSONObject obj=getData();
									obj.put("lastpub", Long.valueOf(C.toHoursSinceEpoc()));
									this.setData(obj);
								}
							}
						}
						if(already > 0)
							str.append(L("@x1 establishment(s) already had your book on their shelves.  ",""+already));
						if(illegal > 0)
							str.append(L("@x1 establishment(s) refused because your title was already taken.  ",""+illegal));
						if(failed > 0)
							str.append(L("@x1 establishment(s) refused because your title was used for something else.  ",""+illegal));
						commonTell(mob,str.toString());
					}
				}
			}
		}
		super.unInvoke();
	}
	
	@Override
	public void executeMsg(Environmental myHost, CMMsg msg)
	{
		if(affected instanceof Item)
		{
			final Item I=(Item)affected;
			if((msg.tool()==I)
			&&(msg.targetMinor()==CMMsg.TYP_BUY))
			{
				final Ability copyA = I.fetchEffect("Copyright");
				if((copyA != null)&&(copyA.text().length()>0))
				{
					final MOB M=CMLib.players().getLoadPlayer(copyA.text());
					if(M!=null)
					{
						final Publishing pubA=(Publishing)M.fetchAbility("Publishing");
						if(pubA!=null)
						{
							final ShopKeeper SK=CMLib.coffeeShops().getShopKeeper(msg.target());
							int royalties = 0;
							if(SK != null)
							{
								royalties = 1;
								final int price=SK.getShop().stockPrice(msg.tool());
								if(price>10)
									royalties = (int)Math.round(CMath.div(price, 10.0));
								CMLib.beanCounter().giveSomeoneMoney(M, CMLib.beanCounter().getCurrency(M), royalties);
							}
							final MiniJSON.JSONObject obj=pubA.getData();
							if(!obj.containsKey(I.Name()))
								obj.put(I.Name(), new MiniJSON.JSONObject());
							try
							{
								MiniJSON.JSONObject bookObj = obj.getCheckedJSONObject(I.Name());
								if(!bookObj.containsKey("copies_sold"))
									bookObj.put("copies_sold", Integer.valueOf(1));
								else
								{
									Long oldVal = bookObj.getCheckedLong("copies_sold");
									bookObj.put("copies_sold", Long.valueOf(oldVal.longValue() + 1));
								}
								if(royalties > 0)
								{
									if(!bookObj.containsKey("paid"))
										bookObj.put("paid", Integer.valueOf(royalties));
									else
									{
										Long oldVal = bookObj.getCheckedLong("paid");
										bookObj.put("paid", Long.valueOf(oldVal.longValue() + royalties));
									}
								}
								if((msg.source().isPlayer())
								&&(M.isPlayer())
								&&(!msg.source().playerStats().getLastIP().equalsIgnoreCase(M.playerStats().getLastIP()))
								&&((M.playerStats().getAccount()==null)||(M.playerStats().getAccount()!=msg.source().playerStats().getAccount())))
								{
									if(!bookObj.containsKey("popularity"))
										bookObj.put("popularity", Integer.valueOf(1));
									else
									{
										Long oldVal = bookObj.getCheckedLong("popularity");
										bookObj.put("popularity", Long.valueOf(oldVal.longValue() + 1));
									}
								}
								pubA.setData(obj);
							}
							catch (MJSONException e)
							{
								Log.errOut(e);
							}
						}
					}
				}
			}
		}
		else
			super.executeMsg(myHost, msg);
	}

	@Override
	public boolean invoke(MOB mob, List<String> commands, Physical givenTarget, boolean auto, int asLevel)
	{
		if(super.checkStop(mob, commands))
			return true;
		if(commands.size()<2)
		{
			commonTell(mob,L("Publish what, at what asking price?"));
			return false;
		}
		
		final Room R=mob.location();
		if(R==null)
			return false;
		final Area A=R.getArea();
		if(A==null)
			return false;
		final TimeClock C=A.getTimeObj();
		final MiniJSON.JSONObject obj=getData();
		if(obj.containsKey("lastpub"))
		{
			try
			{
				Long L=obj.getCheckedLong("lastpub");
				TimeClock lastPubC=(TimeClock)CMClass.getCommon("DefaultTimeClock");
				lastPubC.setFromHoursSinceEpoc(L.longValue());
				if(C.getYear() == lastPubC.getYear())
				{
					if(C.getMonth() <= lastPubC.getMonth())
					{
						commonTell(mob,L("You won't be able to publish any more books this month."));
						return false;
					}
				}
				else
				if(C.getYear() < lastPubC.getYear())
				{
					commonTell(mob,L("You last published in the year @x1?!!",""+lastPubC.getYear()));
					return false;
				}
			}
			catch (MJSONException e)
			{
				Log.errOut(e);
			}
		}

		int startHere = -1;
		for(int i=commands.size()-1;i>=1;i--)
		{
			if(CMath.isNumber(commands.get(i)))
			{
				startHere=i;
				break;
			}
		}
		if(startHere < 0)
		{
			commonTell(mob,L("You haven't specified an asking price."));
			return false;
		}
		price = CMLib.english().matchAnyDenomination(CMLib.beanCounter().getCurrency(mob), CMParms.combine(commands,startHere));
		Item target = super.getTarget(mob, mob.location(), givenTarget, commands, Wearable.FILTER_UNWORNONLY);
		if((target==null)||(!CMLib.flags().canBeSeenBy(target,mob)))
		{
			commonTell(mob,L("You don't seem to have a '@x1'.",(commands.get(0))));
			return false;
		}
		
		if((target.material()&RawMaterial.MATERIAL_MASK)!=RawMaterial.MATERIAL_PAPER)
		{
			commonTell(mob,L("You can't publish something like that."));
			return false;
		}
		if(!CMLib.flags().isReadable(target))
		{
			commonTell(mob,L("That's not even readable!"));
			return false;
		}
		
		String brand = getBrand(target);
		if((brand==null)||(brand.length()==0))
		{
			commonTell(mob,L("You aren't permitted to publish that."));
			return false;
		}
		if(!target.isGeneric())
		{
			commonTell(mob,L("You aren't able to publish that."));
			return false;
		}
		

		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;
		verb=L("publishing @x1",target.name());
		displayText=L("You are @x1",verb);
		found=target;
		success=true;
		if(!proficiencyCheck(mob,0,auto))
			success=false;
		final int duration=getDuration(30,mob,1,3);
		final CMMsg msg=CMClass.getMsg(mob,target,this,getActivityMessageType(),L("<S-NAME> start(s) publishing <T-NAME>."));
		if(mob.location().okMessage(mob,msg))
		{
			mob.location().send(mob,msg);
			beneficialAffect(mob,mob,asLevel,duration);
		}
		return true;
	}
}
