package main

import (
	"encoding/json"
    "log"
    "net/http"
)

type Bank struct {
	Name string `json:"name"`
	Key string `json:"key"`
}
var bank = []Bank{
	{Key: "1", Name: "SWEDBANK"},
	{Key: "2", Name: "IKANOBANKEN"},
	{Key: "3", Name: "JPMORGAN"},
	{Key: "4", Name: "NORDEA"},
	{Key: "5", Name: "CITIBANK"},
	{Key: "6", Name: "HANDELSBANKEN"},
	{Key: "7", Name: "SBAB"},
	{Key: "8", Name: "HSBC"},
	{Key: "9", Name: "NORDNET"},
}
func writeJsonResponse(w http.ResponseWriter, payload any) {
	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(payload); err != nil {
		log.Printf("Failed to write JSON response: %v", err)
	}
}
func listBanks(w http.ResponseWriter, r *http.Request) {
	writeJsonResponse(w, bank)
}
func findBank(value string, field func(Bank) string) *Bank {
	for i := range bank {
		if field(bank[i]) == value {
			return &bank[i]
		}
	}
	return nil
}


func findBankByName(w http.ResponseWriter, r *http.Request) {
	name := r.URL.Path[len("/bank/find.name/"):]
	b := findBank(name, func(b Bank) string { return b.Name })
	if b != nil {
		writeJsonResponse(w, b)
	} else {
		http.Error(w, "Bank not found", http.StatusNotFound)
	}
}

func findBankByKey(w http.ResponseWriter, r *http.Request) {
	key := r.URL.Path[len("/bank/find.key/"):]
	b := findBank(key, func(b Bank) string { return b.Key })
	if b != nil {
		writeJsonResponse(w, b)
	} else {
		http.Error(w, "Bank not found", http.StatusNotFound)
	}
}








func main() {
    mux := http.NewServeMux()
	mux.HandleFunc("GET /", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("Bank service is running"))
	})
	mux.HandleFunc("GET /bank/list", listBanks)
	mux.HandleFunc("GET /bank/find.name/{name}", findBankByName)
	mux.HandleFunc("GET /bank/find.key/{key}", findBankByKey)
	log.Println("Bank service is running on port 8070...")
    log.Fatal(http.ListenAndServe(":8070", mux))	
}