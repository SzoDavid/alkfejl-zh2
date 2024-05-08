/*
A SABLONBAN SZEREPLŐ KOMMENTEK TÖRLÉSE TILOS!

MIVEL CSAK A FUTTATHATÓ KÓD KERÜL ELLENŐRZÉSRE,
EZÉRT TOVÁBBI KOMMENTEK BESZÚRÁSA NEM MEGENGEDETT!

A hallgató neve:
A hallgató NEPTUN kódja:

A PACKAGE DEFINÍCIÓ, KÖNYVTÁRAK IMPORTÁLÁSA ÉS A
GLOBÁLIS VÁLTOZÓK DEFINÍCIÓJA EZT KÖVETŐEN TÖRTÉNJEN:
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

func formaz(in byte) (out1 byte, out2 byte, err error) {
	if ('a' <= in && in <= 'z') {
		out1 = in + 32
		out2 = in
		return
	} 

	err = fmt.Errorf("A parameter nem kisbetu")
	return
}

// 2. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func ellenoriz(in *bufio.Reader, out chan<- byte) {
	for {
		value, _ := in.ReadByte()
		
		if (value == 'q') {
			close(out)
			break
		}

		if (value == ' ' || value == '\n') {
			continue
		}
			
		upper, original, err := formaz(value) 

		if (err != nil) {
			fmt.Print(err)
			continue
		}

		out <- upper
		out <- original
		out <- ' '
	}
}

// 3. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func kiir(in <-chan byte, out *bufio.Writer) {
	for value := range in {
		if (value != ' ') {
			value = byte(value - 32)
		}
		
		out.WriteByte(value)
		out.Flush()
	}
	wg.Done()
}

// 4. FELADAT MEGOLDÁSA EZT KÖVETŐEN TÖRTÉNJEN:

func main() {
	var c chan byte = make(chan byte, 2)
	in := bufio.NewReader(os.Stdin)
	out := bufio.NewWriter(os.Stderr)

	wg.Add(1)
	go ellenoriz(in, c)
	go kiir(c, out)
	wg.Wait()
}
