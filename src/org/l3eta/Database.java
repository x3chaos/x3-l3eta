package org.l3eta;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class Database {
	private static Mongo m;
	private static DB db;

	public Database(String name) {
		try {
			DBError.initErrors();
			m = new Mongo();
			db = m.getDB(name);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO maybe do a fall back. Or a starter.
		}
	}

	public enum Type {
		INSERT, UPDATE, CREATE, HAS, GET; // Add more.
	}

	private int doFunction(Type type, Object... o) {
		try {
			BasicDBObject temp = null, temp1 = null;
			final String col = o[0].toString();
			switch (type) {
			case INSERT:
				for (BasicDBObject obj : MiscUtil.convertArray(o, 1)) {
					if (!hasObject(col, obj)) {
						db.getCollection(col).insert(obj);
					}
				}
				return 0; // TODO check something for this.
			case GET:
				if (o[1] instanceof BasicDBObject) {
					temp = (BasicDBObject) o[1];
					if (hasObject(col, temp)) {
						return 0;
					}
					return -6;
				}
				break;
			case UPDATE:
				if (o[1] instanceof BasicDBObject
						&& o[2] instanceof BasicDBObject) {
					temp = (BasicDBObject) o[1];
					temp1 = (BasicDBObject) o[2];
					if (hasObject(col, temp)) {
						db.getCollection(col).findAndModify(temp, temp1);
						return 0;
					}
					return -4;
				}
				return -5; // TODO define this.
			case CREATE:
				if (db.collectionExists(col)) {
					return -2;
				}
				db.createCollection(col, null);
				return 0;
			case HAS:
				if (o.length == 1) {
					boolean has = db.collectionExists(col);
					return has ? 100 : 101;
				} else {
					if (o[1] instanceof BasicDBObject) {
						temp = (BasicDBObject) o[1];
						boolean has = db.getCollection(col).find(temp).size() != 0;
						return has ? 100 : 101;
					}
					return -3;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	private BasicDBObject getFrom(String col, BasicDBObject q) {
		return (BasicDBObject) db.getCollection(col).find(q).next();
	}

	public BasicDBObject[] getAll(String col) {
		DBCursor dc = db.getCollection(col).find();
		return dc.toArray().toArray(new BasicDBObject[0]);
	}

	public BasicDBObject get(String col, BasicDBObject q) {
		int e = doFunction(Type.GET, col, q);
		if (e == 0) {
			return getFrom(col, q);
		}
		System.out.println(DBError.getError(e));
		return new BasicDBObject().append("null", null);
	}

	public void createCollection(String name) {
		int e = doFunction(Type.CREATE, name);
		if (e != 0) {
			System.out.println(DBError.getError(e));
		}
	}

	public void addTo(String colName, Object o) {
		int e = doFunction(Type.INSERT, colName, o);
		if (e != 0) {
			System.out.println(DBError.getError(e));
		}
	}

	public void update(String colName, BasicDBObject o, BasicDBObject _o) {
		int e = doFunction(Type.UPDATE, colName, o, _o);
		if (e != 0) {
			System.out.println(DBError.getError(e));
		}
	}

	public boolean hasObject(String colName, BasicDBObject o) {
		int e = doFunction(Type.HAS, colName, o);
		if (e == 100 || e == 101) {
			return e == 100;
		}
		System.out.println(DBError.getError(e));
		return false;
	}

	public boolean hasCol(String col) {
		int e = doFunction(Type.HAS, col);
		if (e == 100 || e == 101) {
			return e == 100;
		}
		System.out.println(DBError.getError(e));
		return false;
	}

	public static class DBError {
		private static HashMap<Integer, String> errors;

		public static void initErrors() {
			errors = new HashMap<Integer, String>();
			errors.put(-100, "Unknown Error, Needs to be defined");
			errors.put(-99, "This should not be happening what so ever..");
			errors.put(-6, "Could not find Object: Returning null.");
			errors.put(-3, "Has Error: Object is not an instanceof DBObject");
			errors.put(-2, "Collection Exists");
			errors.put(-1, "Database Error: Something went wrong.");

		}

		public static String getError(int code) {
			if (errors.containsKey(code))
				return errors.get(code);
			return errors.get(-100);
		}
	}
}
