package org.mtransit.parser.ca_gta_up_express_train;

import static org.mtransit.commons.StringUtils.EMPTY;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.mt.data.MAgency;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// https://www.gotransit.com/en/information-resources/software-developers
// https://www.gotransit.com/fr/ressources-informatives/dveloppeurs-de-logiciel
public class GTAUPExpressTrainAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new GTAUPExpressTrainAgencyTools().start(args);
	}

	@Nullable
	@Override
	public List<Locale> getSupportedLanguages() {
		return LANG_EN_FR;
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "UP Express";
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_TRAIN;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return true;
	}

	@Nullable
	@Override
	public Long convertRouteIdFromShortNameNotSupported(@NotNull String routeShortName) {
		if ("UP".equals(routeShortName)) {
			return 0L;
		}
		return super.convertRouteIdFromShortNameNotSupported(routeShortName);
	}

	@Override
	public boolean defaultRouteLongNameEnabled() {
		return true;
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@SuppressWarnings("SpellCheckingInspection")
	private static final Pattern AEROPORT = Pattern.compile("(a[e|é]roport)", Pattern.CASE_INSENSITIVE);
	private static final Pattern GARE = Pattern.compile("(gare)", Pattern.CASE_INSENSITIVE);

	private static final Pattern UP_EXPRESS = Pattern.compile("(UP Express )", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = AEROPORT.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = GARE.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = UP_EXPRESS.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = STATION.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(getFirstLanguageNN(), tripHeadsign);
	}

	private static final Pattern STATION = Pattern.compile("(station)", Pattern.CASE_INSENSITIVE);

	private static final Pattern UP_EXPRESS_GO = Pattern.compile("(up express|up|go/?)", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = STATION.matcher(gStopName).replaceAll(EMPTY);
		gStopName = UP_EXPRESS_GO.matcher(gStopName).replaceAll(EMPTY);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(getFirstLanguageNN(), gStopName);
	}

	private static final String STOP_CODE_WESTON = "WE";
	private static final int STOP_ID_WESTON = 10000;
	private static final String STOP_CODE_UNION = "UN";
	private static final int STOP_ID_UNION = 10001;
	private static final String STOP_CODE_PEARSON = "PA";
	private static final int STOP_ID_PEARSON = 10002;
	private static final String STOP_CODE_BLOOR = "BL";
	private static final int STOP_ID_BLOOR = 10003;
	private static final String STOP_CODE_MOUNT_DENIS = "MD";
	private static final int STOP_ID_MOUNT_DENIS = 10004;

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection DiscouragedApi
		final String stopId = gStop.getStopId();
		switch (stopId) {
		case STOP_CODE_WESTON:
			return STOP_ID_WESTON;
		case STOP_CODE_UNION:
			return STOP_ID_UNION;
		case STOP_CODE_PEARSON:
			return STOP_ID_PEARSON;
		case STOP_CODE_BLOOR:
			return STOP_ID_BLOOR;
		case STOP_CODE_MOUNT_DENIS:
			return STOP_ID_MOUNT_DENIS;
		default:
			throw new MTLog.Fatal("Unexpected stop ID for %s!", gStop.toStringPlus(true));
		}
	}
}
