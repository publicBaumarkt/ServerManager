package net.baumarkt.servermanager.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

/*
 * Created on 24.05.2020 at 15:41
 * Copyright by M.
 * You are not allowed to decompile this file!
 */
public class GameProfileBuilder {

	private static final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
	private static final String JSON_SKIN = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}";
	private static final String JSON_CAPE = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}";

	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();

	private static final HashMap<UUID, CachedProfile> cache = new HashMap<UUID, CachedProfile>();

	private static long cacheTime = -1;

	public static GameProfile fetch(UUID uuid) throws IOException {
		return fetch(uuid, false);
	}

	public static GameProfile fetch(UUID uuid, boolean forceNew) throws IOException {
		if (!forceNew && cache.containsKey(uuid) && cache.get(uuid).isValid()) {
			return cache.get(uuid).profile;
		} else {
			HttpURLConnection connection = (HttpURLConnection) new URL(String.format(SERVICE_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
			connection.setReadTimeout(5000);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//  String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
				String json = IOUtils.toString( connection.getInputStream(), StandardCharsets.UTF_8);

				GameProfile result = gson.fromJson(json, GameProfile.class);
				cache.put(uuid, new CachedProfile(result));
				return result;
			} else {
				if (!forceNew && cache.containsKey(uuid)) {
					return cache.get(uuid).profile;
				}
				JsonObject error = (JsonObject) new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getErrorStream())).readLine());
				throw new IOException(error.get("error").getAsString() + ": " + error.get("errorMessage").getAsString());
			}
		}
	}

	public static GameProfile getProfile(UUID uuid, String name, String skin) {
		return getProfile(uuid, name, skin, null);
	}

	public static GameProfile getProfile(UUID uuid, String name, String skinUrl, String capeUrl) {
		GameProfile profile = new GameProfile(uuid, name);
		boolean cape = capeUrl != null && !capeUrl.isEmpty();

		List<Object> args = new ArrayList<Object>();
		args.add(System.currentTimeMillis());
		args.add(UUIDTypeAdapter.fromUUID(uuid));
		args.add(name);
		args.add(skinUrl);
		if (cape) args.add(capeUrl);

		profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(String.format(cape ? JSON_CAPE : JSON_SKIN, args.toArray(new Object[args.size()])))));
		return profile;
	}

	public static void setCacheTime(long time) {
		cacheTime = time;
	}

	private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

		public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = (JsonObject) json;
			UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
			String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
			GameProfile profile = new GameProfile(id, name);

			if (object.has("properties")) {
				for (Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"), PropertyMap.class)).entries()) {
					profile.getProperties().put(prop.getKey(), prop.getValue());
				}
			}
			return profile;
		}

		public JsonElement serialize(GameProfile profile, Type type, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			if (profile.getId() != null)
				result.add("id", context.serialize(profile.getId()));
			if (profile.getName() != null)
				result.addProperty("name", profile.getName());
			if (!profile.getProperties().isEmpty())
				result.add("properties", context.serialize(profile.getProperties()));
			return result;
		}
	}

	private static class CachedProfile {

		private final long timestamp = System.currentTimeMillis();
		private final GameProfile profile;

		public CachedProfile(GameProfile profile) {
			this.profile = profile;
		}

		public boolean isValid() {
			return cacheTime < 0 || (System.currentTimeMillis() - timestamp) < cacheTime;
		}
	}
}

