package org.l3eta;

import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class Database {
	private static Mongo m;
	private static DB db;
	private static boolean isLoaded = false;

	// TODO add in checks for database not loaded
	public static void init(String dbname) {
		try {
			m = new Mongo();
			db = m.getDB(dbname);
			isLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum Type {
		INSERT, UPDATE, CREATE, HAS, GET, GETALL; // Add more.
	}

	private static Object[] completeArray(Object[] arr, int index) {
		ArrayList<Object> oArr = new ArrayList<Object>();
		for (int i = index; i < arr.length; i++) {
			oArr.add(arr[i]);
		}
		return oArr.toArray();
	}

	private static int doFunction(Type type, Object... o) {
		if (isLoaded) {
			BasicDBObject temp = null, temp1 = null;
			String col = null;
			switch (type) {
				case INSERT:
					try {
						Object[] arr = completeArray(o, 1);
						if (arr instanceof BasicDBObject[]) {
							col = o[0].toString();
							BasicDBObject[] a = (BasicDBObject[]) arr;
							for(BasicDBObject obj : a) {
								if(!hasObject(col, obj)) {
									db.getCollection(col).insert(obj);
								}
							}
							return 0;
						}
						return 2;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return 1;
				case GETALL:
					try {
						if(db.collectionExists(o[0].toString())) {
							return 0;
						} else {
							return -100;
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					return -100;
				case GET:
					if (o[1] instanceof BasicDBObject) {
						col = o[0].toString();
						temp = (BasicDBObject) o[1];
						if(hasObject(col, temp)) {
							return 0;
						}
						return -6;
					}
					break;
				case UPDATE:
					try {
						col = o[0].toString();
						if (o[1] instanceof BasicDBObject
								&& o[2] instanceof BasicDBObject) {
							temp = (BasicDBObject) o[1];
							temp1 = (BasicDBObject) o[2];
							if (hasObject(col, temp)) {
								db.getCollection(col)
										.findAndModify(temp, temp1);
								return 0;
							}
							return -4;
						}
						return -5;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
				case CREATE:
					try {
						db.createCollection(o[0].toString(), null);
						return 0;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return -2;
				case HAS:
					try {
						if (o[1] instanceof BasicDBObject) {
							col = o[0].toString();
							temp = (BasicDBObject) o[1];
							boolean has = db.getCollection(col).find(temp)
									.size() != 0;
							return has ? 100 : 101;
						}
						return -3;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
			}
		}
		return -1;
	}

	private static boolean throwError(int code) {
		try {
			throw new Exception(DBError.getError(code));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private static BasicDBObject getFrom(String col, BasicDBObject q) {
		return (BasicDBObject) db.getCollection(col).find(q).next();
	}
	
	private static BasicDBObject[] getAll(String col) {
		DBCursor dc = db.getCollection(col).find();
		return dc.toArray().toArray(new BasicDBObject[0]);
	}
	
	public static BasicDBObject[] getAllFrom(String col) {
		int e = doFunction(Type.GETALL, col);
		if(e == 0) {			
			return getAll(col);
		}
		throwError(e);
		return null;
	}

	public static BasicDBObject get(String col, BasicDBObject q) {
		int e = doFunction(Type.GET, col, q);
		if (e == 0) {
			return getFrom(col, q);
		}
		throwError(e);
		return new BasicDBObject().append("null", null);
	}

	public static void createCollection(String name) {
		int e = doFunction(Type.CREATE, name);
		if (e != 0) {
			throwError(e);
		}
	}

	public static void addTo(String colName, BasicDBObject o) {
		int e = doFunction(Type.INSERT, o);
		if (e != 0) {
			throwError(e);
		}
	}

	public static void update(String colName, BasicDBObject o, BasicDBObject _o) {
		// db.getCollection(colName).findAndModify(o, _o);
		int e = doFunction(Type.UPDATE, colName, o, _o);
		if (e != 0) {
			throwError(e);
		}
	}

	public static BasicDBObject createQuery(String key, Object val) {
		return new BasicDBObject().append(key, val);
	}

	public static boolean hasObject(String colName, BasicDBObject o) {
		int e = doFunction(Type.HAS, colName, o);
		return e == 100 ? true : e == 101 ? false : throwError(e);
	}
	
	public static class DBError {
		private static HashMap<Integer, String> errors;
		
		public static void initErrors() {
			errors = new HashMap<Integer, String>();	
			errors.put(-100, "Unknown Error, Needs to be defined");
			errors.put(-6, "Could not find Object: Returning null.");
			errors.put(-3, "Has Error: Object is not an instanceof DBObject");
			errors.put(-2, "Unknown: Create Error");
			errors.put(-1, "Not Connected");
			errors.put(2, "Insert Error: Wrong type of Array");
			errors.put(1, "Insert Error: Cannot Insert");
			
		}
		
		public static String getError(int code) {
			if(errors.containsKey(code)) 
				return errors.get(code);
			return errors.get(-100);
		}
	}
}
