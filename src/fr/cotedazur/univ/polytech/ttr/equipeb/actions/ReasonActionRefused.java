package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

public enum ReasonActionRefused {
    ACTION_INVALID(false),
    DESTINATION_CARDS_DECK_EMPTY(false),
    DESTINATION_CARDS_CHOSEN_CARDS_EMPTY(false),
    ROUTE_INVALID(false),
    ROUTE_WANTED_ROUTE_NOT_FOUND(false),
    ROUTE_WANTED_ROUTE_ALREADY_CLAIMED(false),
    ROUTE_NOT_ENOUGH_WAGONS(false),
    ROUTE_WRONG_GIVEN_WAGON_CARDS_SIZE(false),
    ROUTE_NOT_ENOUGH_WAGON_CARDS(false),
    ROUTE_FERRY_NOT_ENOUGH_LOCOMOTIVES(false),
    ROUTE_FERRY_WRONG_WAGON_CARDS_COLOR(false),
    ROUTE_TRAIN_WRONG_WAGON_CARDS_COLOR(false),
    ROUTE_TUNNEL_WRONG_WAGON_CARDS_COLOR(false),
    ROUTE_TUNNEL_NOT_ENOUGH_WAGON_CARDS(true),
    ROUTE_TUNNEL_NOT_ENOUGH_REMOVED_WAGON_CARDS(true),
    STATION_NOT_ENOUGH_STATIONS_LEFT(false),
    STATION_CITY_NOT_VALID(false),
    STATION_NOT_ENOUGH_WAGON_CARDS(false),
    STATION_WRONG_WAGON_CARDS_COLOR(false),
    WAGON_CARDS_DECK_EMPTY(false),
    WAGON_CARDS_ACTION_INVALID(false),
    CHOSEN_ROUTE_NOT_CONNECTED_TO_STATION(false);

    private final boolean skipTurn;

    public boolean isActionSkipTurn() {
        return skipTurn;
    }

    ReasonActionRefused(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }
}
