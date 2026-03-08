"""
Lottttto15 — Русское лото (Russian Lotto)

Правила:
  - Бочонки пронумерованы от 1 до 90.
  - Карточка содержит 15 чисел (3 ряда по 5 чисел в каждом).
  - Ведущий достаёт бочонки по одному и называет число.
  - Игрок зачёркивает соответствующее число на своей карточке, если оно там есть.
  - Побеждает тот, кто первым закроет все 15 чисел на карточке.
"""

import random
from typing import List, Optional


TOTAL_BARRELS = 90
CARD_ROWS = 3
NUMBERS_PER_ROW = 5
CARD_SIZE = CARD_ROWS * NUMBERS_PER_ROW  # 15 чисел


class Card:
    """Игровая карточка лото с 15 числами (3×5)."""

    def __init__(self, numbers: Optional[List[int]] = None):
        if numbers is not None:
            if len(numbers) != CARD_SIZE:
                raise ValueError(
                    f"Карточка должна содержать ровно {CARD_SIZE} чисел, "
                    f"получено {len(numbers)}."
                )
            if len(set(numbers)) != CARD_SIZE:
                raise ValueError("Числа на карточке должны быть уникальными.")
            if any(n < 1 or n > TOTAL_BARRELS for n in numbers):
                raise ValueError(
                    f"Каждое число на карточке должно быть от 1 до {TOTAL_BARRELS}."
                )
            self._numbers: List[int] = list(numbers)
        else:
            self._numbers = random.sample(range(1, TOTAL_BARRELS + 1), CARD_SIZE)

        self._crossed: set = set()

    @property
    def numbers(self) -> List[int]:
        """Все числа на карточке."""
        return list(self._numbers)

    @property
    def crossed(self) -> set:
        """Зачёркнутые числа."""
        return set(self._crossed)

    def mark(self, number: int) -> bool:
        """Зачеркнуть число, если оно есть на карточке.

        Returns:
            True — число было на карточке и зачеркнуто.
            False — числа нет на карточке.
        """
        if number in self._numbers and number not in self._crossed:
            self._crossed.add(number)
            return True
        return False

    def is_complete(self) -> bool:
        """Все числа закрыты — карточка выиграла."""
        return len(self._crossed) == CARD_SIZE

    def remaining(self) -> List[int]:
        """Числа, ещё не зачёркнутые."""
        return [n for n in self._numbers if n not in self._crossed]

    def __str__(self) -> str:
        rows = []
        for row_idx in range(CARD_ROWS):
            row_numbers = self._numbers[row_idx * NUMBERS_PER_ROW:(row_idx + 1) * NUMBERS_PER_ROW]
            cells = []
            for n in row_numbers:
                if n in self._crossed:
                    cells.append(f"[{n:2d}]")
                else:
                    cells.append(f" {n:2d} ")
            rows.append("  ".join(cells))
        return "\n".join(rows)


class Barrel:
    """Мешок с бочонками — генератор случайных чисел."""

    def __init__(self):
        self._pool: List[int] = list(range(1, TOTAL_BARRELS + 1))
        random.shuffle(self._pool)
        self._drawn: List[int] = []

    @property
    def drawn(self) -> List[int]:
        """Уже вытащенные бочонки."""
        return list(self._drawn)

    def remaining_count(self) -> int:
        """Количество оставшихся бочонков."""
        return len(self._pool)

    def draw(self) -> Optional[int]:
        """Достать один бочонок из мешка.

        Returns:
            Число от 1 до 90, или None если мешок пуст.
        """
        if not self._pool:
            return None
        number = self._pool.pop()
        self._drawn.append(number)
        return number

    def is_empty(self) -> bool:
        """Мешок пуст."""
        return len(self._pool) == 0


class Player:
    """Игрок лото."""

    def __init__(self, name: str, card: Optional[Card] = None):
        if not name or not name.strip():
            raise ValueError("Имя игрока не может быть пустым.")
        self.name: str = name.strip()
        self.card: Card = card if card is not None else Card()

    def play_turn(self, number: int) -> bool:
        """Обработать вытащенный бочонок.

        Returns:
            True — число зачёркнуто на карточке.
        """
        return self.card.mark(number)

    def has_won(self) -> bool:
        """Игрок победил."""
        return self.card.is_complete()

    def __str__(self) -> str:
        status = "ПОБЕДИТЕЛЬ! 🎉" if self.has_won() else f"осталось {len(self.card.remaining())} чисел"
        return f"{self.name} ({status})\n{self.card}"


class Game:
    """Игра в лото."""

    def __init__(self, players: List[Player]):
        if len(players) < 1:
            raise ValueError("В игре должен участвовать хотя бы один игрок.")
        self.players: List[Player] = players
        self.barrel: Barrel = Barrel()
        self.winners: List[Player] = []
        self._last_number: Optional[int] = None

    @property
    def last_number(self) -> Optional[int]:
        """Последний вытащенный бочонок."""
        return self._last_number

    def step(self) -> Optional[int]:
        """Сделать один ход: вытащить бочонок и обновить карточки.

        Returns:
            Вытащенное число, или None если мешок пуст.
        """
        number = self.barrel.draw()
        if number is None:
            return None

        self._last_number = number
        for player in self.players:
            if not player.has_won():
                player.play_turn(number)
                if player.has_won() and player not in self.winners:
                    self.winners.append(player)

        return number

    def is_over(self) -> bool:
        """Игра завершена (все игроки победили или мешок пуст)."""
        all_won = all(p.has_won() for p in self.players)
        return all_won or self.barrel.is_empty()

    def play(self) -> List[Player]:
        """Провести полную игру до конца.

        Returns:
            Список победителей в порядке победы.
        """
        while not self.is_over():
            self.step()
        return self.winners
