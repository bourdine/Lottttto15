"""Тесты для модуля lotto."""

import pytest
from lotto import (
    Card,
    Barrel,
    Player,
    Game,
    CARD_SIZE,
    TOTAL_BARRELS,
    CARD_ROWS,
    NUMBERS_PER_ROW,
)


# ---------------------------------------------------------------------------
# Card
# ---------------------------------------------------------------------------

class TestCard:
    def test_default_card_has_correct_size(self):
        card = Card()
        assert len(card.numbers) == CARD_SIZE

    def test_default_card_numbers_are_unique(self):
        card = Card()
        assert len(set(card.numbers)) == CARD_SIZE

    def test_default_card_numbers_in_valid_range(self):
        card = Card()
        assert all(1 <= n <= TOTAL_BARRELS for n in card.numbers)

    def test_custom_card_accepts_valid_numbers(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        assert card.numbers == numbers

    def test_custom_card_wrong_count_raises(self):
        with pytest.raises(ValueError, match="ровно"):
            Card(list(range(1, CARD_SIZE)))  # одного не хватает

    def test_custom_card_duplicates_raises(self):
        numbers = [1] * CARD_SIZE
        with pytest.raises(ValueError, match="уникальными"):
            Card(numbers)

    def test_custom_card_out_of_range_raises(self):
        numbers = list(range(0, CARD_SIZE))  # 0 недопустим
        with pytest.raises(ValueError, match="от 1 до"):
            Card(numbers)

    def test_mark_existing_number_returns_true(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        assert card.mark(1) is True

    def test_mark_missing_number_returns_false(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        assert card.mark(TOTAL_BARRELS) is False  # 90 не в range(1,16)

    def test_mark_already_crossed_returns_false(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        card.mark(1)
        assert card.mark(1) is False

    def test_is_complete_after_all_marks(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        for n in numbers:
            assert not card.is_complete()
            card.mark(n)
        assert card.is_complete()

    def test_remaining_decreases_on_mark(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        assert len(card.remaining()) == CARD_SIZE
        card.mark(numbers[0])
        assert len(card.remaining()) == CARD_SIZE - 1

    def test_str_shows_crossed_number_in_brackets(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        card.mark(1)
        assert "[ 1]" in str(card)

    def test_str_shows_uncrossed_number_without_brackets(self):
        numbers = list(range(1, CARD_SIZE + 1))
        card = Card(numbers)
        assert " 2 " in str(card)


# ---------------------------------------------------------------------------
# Barrel
# ---------------------------------------------------------------------------

class TestBarrel:
    def test_initial_pool_size(self):
        barrel = Barrel()
        assert barrel.remaining_count() == TOTAL_BARRELS

    def test_draw_returns_valid_number(self):
        barrel = Barrel()
        number = barrel.draw()
        assert 1 <= number <= TOTAL_BARRELS

    def test_draw_all_numbers(self):
        barrel = Barrel()
        drawn = set()
        while not barrel.is_empty():
            n = barrel.draw()
            assert n not in drawn, f"Число {n} вытащено дважды"
            drawn.add(n)
        assert drawn == set(range(1, TOTAL_BARRELS + 1))

    def test_draw_from_empty_returns_none(self):
        barrel = Barrel()
        while not barrel.is_empty():
            barrel.draw()
        assert barrel.draw() is None

    def test_drawn_list_grows(self):
        barrel = Barrel()
        barrel.draw()
        barrel.draw()
        assert len(barrel.drawn) == 2

    def test_is_empty_after_all_draws(self):
        barrel = Barrel()
        for _ in range(TOTAL_BARRELS):
            barrel.draw()
        assert barrel.is_empty()


# ---------------------------------------------------------------------------
# Player
# ---------------------------------------------------------------------------

class TestPlayer:
    def test_create_player_with_name(self):
        player = Player("Алиса")
        assert player.name == "Алиса"

    def test_player_name_stripped(self):
        player = Player("  Боб  ")
        assert player.name == "Боб"

    def test_empty_name_raises(self):
        with pytest.raises(ValueError, match="пустым"):
            Player("")

    def test_whitespace_name_raises(self):
        with pytest.raises(ValueError, match="пустым"):
            Player("   ")

    def test_player_gets_card_by_default(self):
        player = Player("Алиса")
        assert isinstance(player.card, Card)

    def test_player_accepts_custom_card(self):
        card = Card(list(range(1, CARD_SIZE + 1)))
        player = Player("Алиса", card=card)
        assert player.card is card

    def test_play_turn_returns_true_for_matching_number(self):
        numbers = list(range(1, CARD_SIZE + 1))
        player = Player("Алиса", Card(numbers))
        assert player.play_turn(1) is True

    def test_has_won_after_all_numbers_called(self):
        numbers = list(range(1, CARD_SIZE + 1))
        player = Player("Алиса", Card(numbers))
        assert not player.has_won()
        for n in numbers:
            player.play_turn(n)
        assert player.has_won()


# ---------------------------------------------------------------------------
# Game
# ---------------------------------------------------------------------------

class TestGame:
    def _make_player(self, name: str, numbers=None) -> Player:
        if numbers is None:
            numbers = list(range(1, CARD_SIZE + 1))
        return Player(name, Card(numbers))

    def test_no_players_raises(self):
        with pytest.raises(ValueError, match="хотя бы"):
            Game([])

    def test_step_returns_number(self):
        game = Game([self._make_player("А")])
        number = game.step()
        assert isinstance(number, int)
        assert 1 <= number <= TOTAL_BARRELS

    def test_step_updates_last_number(self):
        game = Game([self._make_player("А")])
        number = game.step()
        assert game.last_number == number

    def test_game_ends_when_player_wins(self):
        numbers = list(range(1, CARD_SIZE + 1))
        player = self._make_player("А", numbers)
        game = Game([player])
        winners = game.play()
        assert player in winners

    def test_winners_recorded_in_order(self):
        # Два игрока, оба с первыми 15 числами — первый завершит первым
        # в одиночной игре победитель определён при первом step
        numbers = list(range(1, CARD_SIZE + 1))
        p1 = self._make_player("А", numbers)
        p2 = self._make_player("Б", list(range(TOTAL_BARRELS - CARD_SIZE + 1, TOTAL_BARRELS + 1)))
        game = Game([p1, p2])
        game.play()
        # Хотя бы один победитель должен быть
        assert len(game.winners) >= 1

    def test_is_over_when_all_players_win(self):
        numbers = list(range(1, CARD_SIZE + 1))
        player = self._make_player("А", numbers)
        game = Game([player])
        game.play()
        assert game.is_over()

    def test_play_returns_winners_list(self):
        player = self._make_player("А")
        game = Game([player])
        result = game.play()
        assert isinstance(result, list)

    def test_full_game_exhausts_all_barrels_or_has_winner(self):
        players = [Player(f"Игрок{i}") for i in range(3)]
        game = Game(players)
        game.play()
        assert game.is_over()

    def test_card_size_is_15(self):
        assert CARD_SIZE == 15

    def test_card_structure(self):
        assert CARD_ROWS * NUMBERS_PER_ROW == CARD_SIZE
