/*
A SABLONBAN SZEREPLŐ KOMMENTEK TÖRLÉSE TILOS!

MIVEL CSAK A FUTTATHATÓ KÓD KERÜL ELLENŐRZÉSRE,
EZÉRT TOVÁBBI KOMMENTEK BESZÚRÁSA NEM MEGENGEDETT!

A hallgató neve:
A hallgató NEPTUN kódja:

A PACKAGE DEFINÍCIÓ, KÖNYVTÁRAK IMPORTÁLÁSA ÉS A
GLOBÁLIS VÁLTOZÓK DEFINÍCIÓJA EZT KÖVETŐEN TÖRTÉNJEN:

TANDI A MINUSZT ÉS A PLUSZT CSERÉLD KI ÉS A VÉGÉN KIIRATÁS ELŐTT NEM KELL MÓDOSÍTANI A VALUET

*/

package main

import (
	"bufio"
	"fmt"
	"os"
	"sync"
)

var wg sync.WaitGroup

// 1. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func formaz(input byte) (output1 byte, output2 byte, err error) {
	if 'a' <= input && input <= 'z' {
		output1 = input
		output2 = input + 32
		return
	} else {
		err = fmt.Errorf("hiba ")
		return
	}
}

// 2. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func ellenoriz(in *bufio.Reader, out chan<- byte) {

	for {
		value, _ := in.ReadByte()

		if value == 'q' {
			break
		}

		if value != ' ' && value != '\n' {
			a, b, err := formaz(value)

			if err != nil {
				fmt.Print(err)
			} else {
				out <- a
				out <- b
				out <- ' '
			}
		}
	}

	close(out)
}

// 3. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func kiir(in <-chan byte, out *bufio.Writer) {

	for value := range in {
		if value == ' ' {
			fmt.Printf(" ")
		}
		out.WriteByte(byte(value - 32))
		out.Flush()
	}
	wg.Done()
}

// 4. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func main() {

	var c chan byte = make(chan byte)
	in := bufio.NewReader(os.Stdin)
	out := bufio.NewWriter(os.Stderr)

	wg.Add(1)
	go ellenoriz(in, c)
	go kiir(c, out)
	wg.Wait()
}
