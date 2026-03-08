"""Командный интерфейс для игры Lottttto15."""

import sys
from lotto import Game, Player, Card, CARD_SIZE


def print_separator(char: str = "-", width: int = 60) -> None:
    print(char * width)


def print_state(game: Game, number: int) -> None:
    print_separator()
    print(f"Вытащен бочонок: {number}")
    print_separator()
    for player in game.players:
        print(player)
        print()


def get_player_count() -> int:
    while True:
        try:
            count = int(input("Введите количество игроков (1–6): ").strip())
            if 1 <= count <= 6:
                return count
            print("Пожалуйста, введите число от 1 до 6.")
        except ValueError:
            print("Некорректный ввод, попробуйте снова.")


def get_player_name(index: int) -> str:
    while True:
        name = input(f"Введите имя игрока {index + 1}: ").strip()
        if name:
            return name
        print("Имя не может быть пустым.")


def run_auto_game(players: list) -> None:
    """Провести автоматическую игру без пауз."""
    game = Game(players)
    winners = game.play()
    print_separator("=")
    print("Игра завершена!")
    if winners:
        print(f"Победитель(и): {', '.join(p.name for p in winners)}")
    else:
        print("Победителей нет (мешок опустел).")
    print_separator("=")


def run_interactive_game(players: list) -> None:
    """Провести игру в интерактивном режиме (нажатие Enter для каждого хода)."""
    game = Game(players)
    print_separator("=")
    print("Игра началась! Нажимайте Enter для розыгрыша следующего бочонка.")
    print_separator("=")

    while not game.is_over():
        input("  [Enter] — достать бочонок...")
        number = game.step()
        if number is None:
            break
        print_state(game, number)
        for player in game.players:
            if player.has_won():
                print(f"  🎉 {player.name} закрыл(а) всю карточку!")

    print_separator("=")
    print("Игра завершена!")
    if game.winners:
        print(f"Победитель(и): {', '.join(p.name for p in game.winners)}")
    else:
        print("Победителей нет (мешок опустел).")
    print_separator("=")


def main() -> int:
    print("=" * 60)
    print("         Lottttto15 — Русское Лото")
    print(f"   Числа от 1 до {TOTAL_BARRELS}, {CARD_SIZE} чисел на карточке")
    print("=" * 60)

    count = get_player_count()
    players = [Player(get_player_name(i)) for i in range(count)]

    print("\nРежим игры:")
    print("  1 — Автоматический (быстрый результат)")
    print("  2 — Интерактивный (ход за ходом)")
    while True:
        choice = input("Ваш выбор [1/2]: ").strip()
        if choice in ("1", "2"):
            break
        print("Введите 1 или 2.")

    if choice == "1":
        run_auto_game(players)
    else:
        run_interactive_game(players)

    return 0


if __name__ == "__main__":
    sys.exit(main())
